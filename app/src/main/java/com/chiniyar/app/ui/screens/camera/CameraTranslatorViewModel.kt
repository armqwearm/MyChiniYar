package com.chiniyar.app.ui.screens.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.chiniyar.app.data.analysis.AnalyzedWord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CameraTranslatorUiState(
    val imageUri: Uri? = null,
    val extractedText: String = "",
    val translatedText: String = "",
    val words: List<AnalyzedWord> = emptyList(),
    val isProcessing: Boolean = false,
    val statusMessage: String = "",
    val error: String? = null
)

class CameraTranslatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CameraTranslatorUiState())
    val uiState: StateFlow<CameraTranslatorUiState> = _uiState.asStateFlow()

    fun setImage(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            imageUri = uri,
            extractedText = "",
            translatedText = "",
            words = emptyList(),
            isProcessing = false,
            statusMessage = "",
            error = null
        )
    }

    fun setExtractedText(text: String) {
        _uiState.value = _uiState.value.copy(extractedText = text, error = null)
    }

    fun setTranslatedText(text: String) {
        _uiState.value = _uiState.value.copy(
            translatedText = text,
            isProcessing = false,
            statusMessage = "",
            error = null
        )
    }

    fun setWords(words: List<AnalyzedWord>) {
        _uiState.value = _uiState.value.copy(words = words)
    }

    fun setWordSaved(word: String, saved: Boolean) {
        _uiState.value = _uiState.value.copy(
            words = _uiState.value.words.map {
                if (it.word == word) it.copy(saved = saved) else it
            }
        )
    }

    fun setProcessing(value: Boolean, status: String = "") {
        _uiState.value = _uiState.value.copy(
            isProcessing = value,
            statusMessage = status,
            error = if (value) null else _uiState.value.error
        )
    }

    fun setError(message: String?) {
        _uiState.value = _uiState.value.copy(
            isProcessing = false,
            statusMessage = "",
            error = message
        )
    }

    fun clearResults() {
        _uiState.value = CameraTranslatorUiState()
    }
}
