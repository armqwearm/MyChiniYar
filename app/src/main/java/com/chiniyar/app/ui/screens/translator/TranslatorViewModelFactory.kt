package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chiniyar.app.di.AppContainer

class TranslatorViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(TranslatorViewModel::class.java)) {
            "Unsupported ViewModel: ${modelClass.name}"
        }
        return TranslatorViewModel(container.translateTextUseCase) as T
    }
}
