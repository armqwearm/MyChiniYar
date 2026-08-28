package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiniyar.app.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TranslatorUiState(
    val sourceText: String = "",
    val translatedText: String = "",
    val sourceLanguage: String = "چینی",
    val targetLanguage: String = "فارسی",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TranslatorViewModel(
    private val translateText: TranslateTextUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    fun onSourceTextChanged(value: String) {
        _uiState.value = _uiState.value.copy(sourceText = value, errorMessage = null)
    }

    fun swapLanguages() {
        val state = _uiState.value
        _uiState.value = state.copy(
            sourceLanguage = state.targetLanguage,
            targetLanguage = state.sourceLanguage,
            sourceText = state.translatedText,
            translatedText = state.sourceText
        )
    }

    fun translate() {
        val state = _uiState.value
        if (state.sourceText.isBlank()) return
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            runCatching {
                translateText(state.sourceText, state.sourceLanguage, state.targetLanguage)
            }.onSuccess { result ->
                _uiState.value = _uiState.value.copy(isLoading = false, translatedText = result)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "خطایی در ترجمه رخ داد"
                )
            }
        }
    }
}
