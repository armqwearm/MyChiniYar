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

/** Application-scoped dependency graph. Heavy ML/database components are initialized lazily. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val translationRepository by lazy { InMemoryTranslationRepository() }
    private val dictionaryRepository by lazy { InMemoryDictionaryRepository() }

    val userPreferencesRepository by lazy { UserPreferencesRepository(appContext) }
    val translateTextUseCase by lazy { TranslateTextUseCase(translationRepository) }
    val searchDictionaryUseCase by lazy { SearchDictionaryUseCase(dictionaryRepository) }

    // Shared camera-translation dependencies are created only when the camera feature is opened.
    private val cameraTranslator by lazy { OfflineChinesePersianTranslator() }
    val translationManager by lazy { TranslationManager(cameraTranslator) }
    val vocabularyDatabase by lazy { VocabularyDatabase.getInstance(appContext) }
    val chineseWordAnalyzer by lazy { ChineseWordAnalyzer(appContext) }
    val chineseOcrProcessor by lazy { ChineseOcrProcessor() }

    val cameraTranslationUseCase by lazy {
        CameraTranslationUseCase(
            ocrProcessor = chineseOcrProcessor,
            translationManager = translationManager,
            analyzer = chineseWordAnalyzer,
            vocabularyDb = vocabularyDatabase
        )
    }
}
