package com.chiniyar.app.data.translation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Coordinates model preparation and on-device Chinese/Persian translation. */
class TranslationManager(
    private val translator: OfflineChinesePersianTranslator = OfflineChinesePersianTranslator()
) {
    private val _state = MutableStateFlow<TranslationModelState>(TranslationModelState.NotReady)
    val state: StateFlow<TranslationModelState> = _state.asStateFlow()

    suspend fun prepare(): Result<Unit> {
        if (_state.value is TranslationModelState.Ready) return Result.success(Unit)
        _state.value = TranslationModelState.Preparing
        return translator.prepareModel().onSuccess {
            _state.value = TranslationModelState.Ready
        }.onFailure { error ->
            _state.value = TranslationModelState.Failed(
                error.message ?: "مدل ترجمه آماده نشد"
            )
        }
    }

    suspend fun translateChineseToPersian(text: String): Result<String> = translate {
        translator.translateChineseToPersian(text)
    }

    suspend fun translatePersianToChinese(text: String): Result<String> = translate {
        translator.translatePersianToChinese(text)
    }

    /** Camera pipeline compatibility: Chinese -> Persian. */
    suspend fun translate(text: String): Result<String> = translateChineseToPersian(text)

    fun close() = translator.close()

    private suspend fun translate(action: suspend () -> String): Result<String> {
        if (_state.value !is TranslationModelState.Ready) {
            return Result.failure(IllegalStateException("Translation model is not ready"))
        }
        return runCatching { action() }
    }
}
