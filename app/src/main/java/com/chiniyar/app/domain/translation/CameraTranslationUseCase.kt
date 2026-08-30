package com.chiniyar.app.domain.translation

import android.content.Context
import android.net.Uri
import com.chiniyar.app.data.analysis.AnalyzedWord
import com.chiniyar.app.data.analysis.ChineseWordAnalyzer
import com.chiniyar.app.data.analysis.OfflineChineseDictionary
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.translation.OfflineChinesePersianTranslator
import com.chiniyar.app.ui.screens.camera.ChineseOcrProcessor

/** Coordinates OCR, translation and word analysis without UI concerns. */
class CameraTranslationUseCase(
    private val ocrProcessor: ChineseOcrProcessor,
    private val translator: OfflineChinesePersianTranslator,
    private val analyzer: ChineseWordAnalyzer,
    private val vocabularyDb: VocabularyDatabase
) {
    suspend fun execute(
        context: Context,
        imageUri: Uri,
        onStatus: (String) -> Unit = {}
    ): Result {
        onStatus("در حال استخراج متن چینی آفلاین...")
        val text = ocrProcessor.recognize(context, imageUri).trim()
        if (text.isBlank()) return Result.failure(IllegalStateException("متن قابل تشخیصی در تصویر پیدا نشد."))

        onStatus("در حال آماده‌سازی ترجمه آفلاین...")
        val translated = translator.translate(text).trim()
        if (translated.isBlank()) return Result.failure(IllegalStateException("ترجمه‌ای برای متن استخراج‌شده دریافت نشد."))

        onStatus("در حال استخراج ۲۰ واژه غیرتکراری...")
        val savedWords = vocabularyDb.allWords()
        val analyzed = analyzer.segment(text).map { word ->
            val localMeaning = OfflineChineseDictionary.meaning(word)
            val meaning = localMeaning ?: runCatching { translator.translate(word) }.getOrDefault("")
            AnalyzedWord(
                word = word,
                pinyin = analyzer.pinyin(word),
                meaning = meaning.ifBlank { "معنی پیدا نشد" },
                saved = word in savedWords
            )
        }

        return Result.success(ResultData(text, translated, analyzed))
    }

    data class ResultData(
        val extractedText: String,
        val translatedText: String,
        val words: List<AnalyzedWord>
    )
}
