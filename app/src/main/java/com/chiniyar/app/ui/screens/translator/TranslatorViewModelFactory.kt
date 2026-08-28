package com.chiniyar.app.ui.screens.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chiniyar.app.data.repository.InMemoryTranslationRepository
import com.chiniyar.app.domain.usecase.TranslateTextUseCase

class TranslatorViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranslatorViewModel::class.java)) {
            val repository = InMemoryTranslationRepository()
            return TranslatorViewModel(TranslateTextUseCase(repository)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
