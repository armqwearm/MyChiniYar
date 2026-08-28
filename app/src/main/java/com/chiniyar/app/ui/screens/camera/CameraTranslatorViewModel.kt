package com.chiniyar.app.ui.screens.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CameraTranslatorUiState(
    val imageUri: Uri? = null,
    val extractedText: String = "",
    val translatedText: String = "",
    val isProcessing: Boolean = false,
    val error: String? = null
)

class CameraTranslatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CameraTranslatorUiState())
    val uiState: StateFlow<CameraTranslatorUiState> = _uiState.asStateFlow()

    fun setImage(uri: Uri?) { _uiState.value = _uiState.value.copy(imageUri = uri) }
    fun setExtractedText(text: String) { _uiState.value = _uiState.value.copy(extractedText = text, error = null) }
    fun setTranslatedText(text: String) { _uiState.value = _uiState.value.copy(translatedText = text, isProcessing = false) }
    fun setProcessing(value: Boolean) { _uiState.value = _uiState.value.copy(isProcessing = value, error = null) }
    fun setError(message: String) { _uiState.value = _uiState.value.copy(isProcessing = false, error = message) }
    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }
}
