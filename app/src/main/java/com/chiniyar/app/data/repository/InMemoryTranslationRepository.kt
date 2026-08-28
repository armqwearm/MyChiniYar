package com.chiniyar.app.data.repository

import com.chiniyar.app.core.common.AppResult
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.core.model.Translation
import com.chiniyar.app.domain.repository.TranslationRepository

/** Temporary local engine used until a remote/ML translation provider is connected. */
class InMemoryTranslationRepository : TranslationRepository {
    private val zhToFa = mapOf(
        "你好" to "سلام",
        "谢谢" to "ممنون",
        "再见" to "خداحافظ",
        "中国" to "چین",
        "北京" to "پکن",
        "上海" to "شانگهای",
        "我爱中国" to "من چین را دوست دارم"
    )

    override suspend fun translate(
        text: String,
        source: Language,
        target: Language
    ): AppResult<Translation> {
        val translated = when {
            source == Language.Chinese && target == Language.Persian -> zhToFa[text] ?: "ترجمه‌ای برای این عبارت در داده محلی پیدا نشد."
            source == Language.Persian && target == Language.Chinese -> zhToFa.entries.firstOrNull { it.value == text }?.key
                ?: "برای این عبارت، ترجمه محلی موجود نیست."
            else -> text
        }
        return AppResult.Success(Translation(text, translated, source, target, confidence = 0.7f))
    }
}
