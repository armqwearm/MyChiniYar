package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.data.translation.TranslationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Screen state and intents for the text translator. */
class TranslatorViewModel(
    private val translationManager: TranslationManager
) : ViewModel() {
    data class UiState(
        val input: String = "",
        val output: String = "",
        val source: Language = Language.CHINESE,
        val target: Language = Language.PERSIAN,
        val isLoading: Boolean = false,
        val statusMessage: String = "",
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setInput(value: String) {
        _uiState.value = _uiState.value.copy(input = value, error = null)
    }

    fun swapLanguages() {
        val state = _uiState.value
        _uiState.value = state.copy(
            source = state.target,
            target = state.source,
            input = state.output,
            output = state.input,
            error = null
        )
    }

    fun translate() {
        val state = _uiState.value
        if (state.input.isBlank()) {
            _uiState.value = state.copy(error = "متنی برای ترجمه وارد کنید")
            return
        }
        if (state.source != Language.CHINESE || state.target != Language.PERSIAN) {
            _uiState.value = state.copy(error = "در Release 1 ترجمه آفلاین چینی ↔ فارسی پشتیبانی می‌شود.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                statusMessage = "در حال آماده‌سازی مدل ترجمه آفلاین...",
                error = null
            )
            val prepared = translationManager.prepare()
            if (prepared.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusMessage = "",
                    error = prepared.exceptionOrNull()?.message ?: "مدل ترجمه آماده نشد."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(statusMessage = "در حال ترجمه...")
            val result = translationManager.translate(state.input)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    output = result.getOrThrow().trim(),
                    isLoading = false,
                    statusMessage = "",
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    statusMessage = "",
                    error = result.exceptionOrNull()?.message ?: "ترجمه انجام نشد."
                )
            }
        }
    }
}
