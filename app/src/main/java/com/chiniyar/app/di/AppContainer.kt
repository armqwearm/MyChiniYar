package com.chiniyar.app.di

import android.content.Context
import com.chiniyar.app.data.preferences.UserPreferencesRepository
import com.chiniyar.app.data.repository.InMemoryTranslationRepository
import com.chiniyar.app.domain.usecase.TranslateTextUseCase

/** Application-scoped dependency graph. Screens never construct repositories directly. */
class AppContainer(context: Context) {
    private val translationRepository = InMemoryTranslationRepository()

    val userPreferencesRepository = UserPreferencesRepository(context.applicationContext)

    val translateTextUseCase: TranslateTextUseCase =
        TranslateTextUseCase(translationRepository)
}
