package com.chiniyar.app.domain.translation

import android.content.Context
import android.net.Uri
import com.chiniyar.app.data.analysis.AnalyzedWord
import com.chiniyar.app.data.analysis.ChineseWordAnalyzer
import com.chiniyar.app.data.analysis.OfflineChineseDictionary
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.translation.TranslationManager
import com.chiniyar.app.ui.screens.camera.ChineseOcrProcessor

/** Coordinates OCR, on-device translation and word analysis without UI concerns. */
class CameraTranslationUseCase(
    private val ocrProcessor: ChineseOcrProcessor,
    private val translationManager: TranslationManager,
    private val analyzer: ChineseWordAnalyzer,
    private val vocabularyDb: VocabularyDatabase
) {
    suspend fun execute(
        context: Context,
        imageUri: Uri,
        onStatus: (String) -> Unit = {},
        onOcrResult: (String) -> Unit = {},
        onWordsResult: (List<AnalyzedWord>) -> Unit = {}
    ): kotlin.Result<ResultData> {
        onStatus("در حال استخراج متن چینی آفلاین...")
        val text = ocrProcessor.recognize(context, imageUri).trim()
        if (text.isBlank()) {
            return kotlin.Result.failure(IllegalStateException("متن قابل تشخیصی در تصویر پیدا نشد."))
        }

        // OCR is an independent result: publish it immediately.
        onOcrResult(text)

        // Analyze and publish local words before any translation-model work.
        onStatus("در حال استخراج ۲۰ واژه غیرتکراری...")
        val savedWords = vocabularyDb.allWords()
        val words = analyzer.segment(text)
        val initialWords = words.map { word ->
            AnalyzedWord(
                word = word,
                pinyin = analyzer.pinyin(word),
                meaning = OfflineChineseDictionary.meaning(word)
                    ?: "معنی در فرهنگ آفلاین موجود نیست",
                saved = word in savedWords
            )
        }
        onWordsResult(initialWords)

        // Full-text translation requires the on-device ML Kit model to be available.
        onStatus("در حال آماده‌سازی مدل ترجمه آفلاین...")
        val preparation = translationManager.prepare()
        if (preparation.isFailure) {
            return kotlin.Result.success(
                ResultData(
                    extractedText = text,
                    translatedText = "",
                    words = initialWords,
                    translationError = preparation.exceptionOrNull()?.message
                        ?: "مدل ترجمه آماده نشد."
                )
            )
        }

        onStatus("در حال ترجمه متن...")
        val translatedResult = translationManager.translate(text)
        if (translatedResult.isFailure) {
            return kotlin.Result.success(
                ResultData(
                    extractedText = text,
                    translatedText = "",
                    words = initialWords,
                    translationError = translatedResult.exceptionOrNull()?.message
                        ?: "ترجمه متن انجام نشد."
                )
            )
        }
        val translated = translatedResult.getOrThrow().trim()
        if (translated.isBlank()) {
            return kotlin.Result.success(
                ResultData(
                    extractedText = text,
                    translatedText = "",
                    words = initialWords,
                    translationError = "ترجمه‌ای برای متن استخراج‌شده دریافت نشد."
                )
            )
        }

        // Fill dictionary misses with one batched on-device request.
        val missing = words.filter { OfflineChineseDictionary.meaning(it).isNullOrBlank() }
        val translatedMeanings = if (missing.isEmpty()) {
            emptyMap()
        } else {
            onStatus("در حال تکمیل معنی واژه‌ها...")
            val batchResult = translationManager.translate(missing.joinToString("\n"))
            if (batchResult.isSuccess) {
                batchResult.getOrThrow().lines().mapIndexedNotNull { index, line ->
                    val meaning = line.trim()
                    missing.getOrNull(index)?.takeIf { meaning.isNotEmpty() }?.let { it to meaning }
                }.toMap()
            } else emptyMap()
        }

        val analyzed = initialWords.map { word ->
            if (word.meaning == "معنی در فرهنگ آفلاین موجود نیست") {
                word.copy(meaning = translatedMeanings[word.word] ?: word.meaning)
            } else word
        }
        onWordsResult(analyzed)

        return kotlin.Result.success(
            ResultData(
                extractedText = text,
                translatedText = translated,
                words = analyzed,
                translationError = null
            )
        )
    }

    data class ResultData(
        val extractedText: String,
        val translatedText: String,
        val words: List<AnalyzedWord>,
        val translationError: String? = null
    )
}
