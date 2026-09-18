package com.chiniyar.app.domain.repository

import com.chiniyar.app.core.common.AppResult
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.core.model.Translation

interface TranslationRepository {
    suspend fun translate(
        text: String,
        source: Language,
        target: Language
    ): AppResult<Translation>
}
