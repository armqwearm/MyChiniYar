package com.chiniyar.app.domain.translation

import android.content.Context
import android.net.Uri
import com.chiniyar.app.data.analysis.AnalyzedWord
import com.chiniyar.app.data.analysis.ChineseWordAnalyzer
import com.chiniyar.app.data.analysis.OfflineChineseDictionary
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.translation.TranslationManager
import com.chiniyar.app.ui.screens.camera.ChineseOcrProcessor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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

        onOcrResult(text)

        onStatus("در حال استخراج ۲۰ واژه غیرتکراری...")
        val savedWords = vocabularyDb.allWords()
        val words = analyzer.segment(text)
        val initialWords = words.map { word ->
            AnalyzedWord(
                word = word,
                pinyin = analyzer.pinyin(word),
                meaning = OfflineChineseDictionary.meaning(word)
                    ?: "در حال یافتن معنی...",
                saved = word in savedWords
            )
        }
        onWordsResult(initialWords)

        onStatus("در حال آماده‌سازی مدل ترجمه آفلاین...")
        val preparation = translationManager.prepare()
        if (preparation.isFailure) {
            return kotlin.Result.success(
                ResultData(
                    extractedText = text,
                    translatedText = "",
                    words = initialWords.map { word ->
                        if (word.meaning == "در حال یافتن معنی...") {
                            word.copy(meaning = "معنی آفلاین در دسترس نیست")
                        } else word
                    },
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
                    words = initialWords.map { word ->
                        if (word.meaning == "در حال یافتن معنی...") {
                            word.copy(meaning = "معنی آفلاین در دسترس نیست")
                        } else word
                    },
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

        // Dictionary hits are already offline. Missing entries are translated one-by-one
        // after the model is ready, avoiding unreliable newline-to-newline batch mapping.
        val missing = words.filter { OfflineChineseDictionary.meaning(it).isNullOrBlank() }
        val translatedMeanings = coroutineScope {
            missing.map { word ->
                async {
                    word to translationManager.translate(word)
                        .getOrDefault("")
                        .trim()
                }
            }.awaitAll().toMap()
        }

        val analyzed = initialWords.map { word ->
            if (word.meaning == "در حال یافتن معنی...") {
                word.copy(
                    meaning = translatedMeanings[word.word]
                        ?.takeIf { it.isNotBlank() }
                        ?: "معنی پیدا نشد"
                )
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
