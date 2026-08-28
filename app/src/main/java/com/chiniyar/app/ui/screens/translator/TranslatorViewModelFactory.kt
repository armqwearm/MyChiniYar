package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chiniyar.app.di.AppContainer

class TranslatorViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranslatorViewModel::class.java)) {
            return TranslatorViewModel(appContainer.translateTextUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
