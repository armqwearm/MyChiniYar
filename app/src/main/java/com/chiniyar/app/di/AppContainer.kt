package com.chiniyar.app.di

import android.content.Context
import com.chiniyar.app.data.analysis.ChineseWordAnalyzer
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.preferences.UserPreferencesRepository
import com.chiniyar.app.data.repository.InMemoryDictionaryRepository
import com.chiniyar.app.data.repository.InMemoryTranslationRepository
import com.chiniyar.app.data.translation.OfflineChinesePersianTranslator
import com.chiniyar.app.data.translation.TranslationManager
import com.chiniyar.app.domain.translation.CameraTranslationUseCase
import com.chiniyar.app.domain.usecase.SearchDictionaryUseCase
import com.chiniyar.app.domain.usecase.TranslateTextUseCase
import com.chiniyar.app.ui.screens.camera.ChineseOcrProcessor

/** Application-scoped dependency graph. Screens never construct repositories directly. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val translationRepository = InMemoryTranslationRepository()
    private val dictionaryRepository = InMemoryDictionaryRepository()

    val userPreferencesRepository = UserPreferencesRepository(appContext)
    val translateTextUseCase = TranslateTextUseCase(translationRepository)
    val searchDictionaryUseCase = SearchDictionaryUseCase(dictionaryRepository)

    // Shared camera-translation dependencies for the lifetime of the application.
    private val cameraTranslator = OfflineChinesePersianTranslator()
    val translationManager = TranslationManager(cameraTranslator)
    val vocabularyDatabase = VocabularyDatabase.getInstance(appContext)
    val chineseWordAnalyzer = ChineseWordAnalyzer()
    val chineseOcrProcessor = ChineseOcrProcessor()

    val cameraTranslationUseCase = CameraTranslationUseCase(
        ocrProcessor = chineseOcrProcessor,
        translationManager = translationManager,
        analyzer = chineseWordAnalyzer,
        vocabularyDb = vocabularyDatabase
    )
}
