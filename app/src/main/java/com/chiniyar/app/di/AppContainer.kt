package com.chiniyar.app.di

import com.chiniyar.app.data.repository.InMemoryTranslationRepository
import com.chiniyar.app.domain.usecase.TranslateTextUseCase

/** Application-scoped dependency graph. Keeps construction out of screens. */
class AppContainer {
    private val translationRepository = InMemoryTranslationRepository()

    val translateTextUseCase: TranslateTextUseCase =
        TranslateTextUseCase(translationRepository)
}
