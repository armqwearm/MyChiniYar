package com.chiniyar.app.domain.usecase

import com.chiniyar.app.core.common.AppResult
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.core.model.Translation
import com.chiniyar.app.domain.repository.TranslationRepository

class TranslateTextUseCase(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(
        text: String,
        source: Language,
        target: Language
    ): AppResult<Translation> {
        val normalized = text.trim()
        if (normalized.isEmpty()) {
            return AppResult.Error("متن برای ترجمه خالی است")
        }
        if (source == target) {
            return AppResult.Success(
                Translation(normalized, normalized, source, target, confidence = 1f)
            )
        }
        return repository.translate(normalized, source, target)
    }
}
