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
        onOcrResult: (String) -> Unit = {}
    ): kotlin.Result<ResultData> {
        onStatus("در حال استخراج متن چینی آفلاین...")
        val text = ocrProcessor.recognize(context, imageUri).trim()
        if (text.isBlank()) {
            return kotlin.Result.failure(IllegalStateException("متن قابل تشخیصی در تصویر پیدا نشد."))
        }

        // OCR is an independent result and must be visible before model preparation.
        onOcrResult(text)

        onStatus("در حال آماده‌سازی مدل ترجمه آفلاین...")
        val preparation = translationManager.prepare()
        if (preparation.isFailure) {
            return kotlin.Result.failure(
                preparation.exceptionOrNull()
                    ?: IllegalStateException("مدل ترجمه آماده نشد.")
            )
        }

        onStatus("در حال ترجمه متن...")
        val translated = translationManager.translate(text).getOrElse { error ->
            return kotlin.Result.failure(error)
        }.trim()
        if (translated.isBlank()) {
            return kotlin.Result.failure(IllegalStateException("ترجمه‌ای برای متن استخراج‌شده دریافت نشد."))
        }

        onStatus("در حال استخراج ۲۰ واژه غیرتکراری...")
        val words = analyzer.segment(text)
        val savedWords = vocabularyDb.allWords()
        val meanings = linkedMapOf<String, String>()
        val missing = words.filter { OfflineChineseDictionary.meaning(it).isNullOrBlank() }

        if (missing.isNotEmpty()) {
            onStatus("در حال تکمیل معنی واژه‌ها...")
            val batch = missing.joinToString(separator = "\n")
            val translatedBatch = translationManager.translate(batch).getOrDefault("")
            translatedBatch.lines().forEachIndexed { index, line ->
                if (index < missing.size && line.isNotBlank()) meanings[missing[index]] = line.trim()
            }
        }

        val analyzed = words.map { word ->
            val meaning = OfflineChineseDictionary.meaning(word)
                ?: meanings[word]
                ?: "معنی پیدا نشد"
            AnalyzedWord(
                word = word,
                pinyin = analyzer.pinyin(word),
                meaning = meaning,
                saved = word in savedWords
            )
        }

        return kotlin.Result.success(ResultData(text, translated, analyzed))
    }

    data class ResultData(
        val extractedText: String,
        val translatedText: String,
        val words: List<AnalyzedWord>
    )
}
