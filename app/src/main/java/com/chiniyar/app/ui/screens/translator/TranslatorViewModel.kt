package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiniyar.app.core.common.AppResult
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Screen-level state holder. The UI only observes state and emits user intents. */
class TranslatorViewModel(
    private val translateText: TranslateTextUseCase
) : ViewModel() {
    data class UiState(
        val input: String = "",
        val output: String = "",
        val source: Language = Language.CHINESE,
        val target: Language = Language.PERSIAN,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setInput(value: String) { _uiState.value = _uiState.value.copy(input = value, error = null) }

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
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = translateText(state.input, state.source, state.target)) {
                is AppResult.Success -> _uiState.value = _uiState.value.copy(
                    output = result.data.translated,
                    isLoading = false
                )
                is AppResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
                is AppResult.Loading -> Unit
            }
        }
    }
}
