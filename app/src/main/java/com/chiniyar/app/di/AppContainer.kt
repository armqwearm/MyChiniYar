package com.chiniyar.app.di

import com.chiniyar.app.data.repository.InMemoryTranslationRepository
import com.chiniyar.app.domain.usecase.TranslateTextUseCase

/** Central application dependency graph. Replace providers here as the app grows. */
class AppContainer {
    private val translationRepository = InMemoryTranslationRepository()
    val translateTextUseCase = TranslateTextUseCase(translationRepository)
}
