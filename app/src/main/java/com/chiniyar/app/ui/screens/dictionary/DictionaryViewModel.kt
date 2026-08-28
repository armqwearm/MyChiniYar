package com.chiniyar.app.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiniyar.app.core.model.DictionaryEntry
import com.chiniyar.app.domain.usecase.SearchDictionaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class DictionaryViewModel(
    private val searchDictionary: SearchDictionaryUseCase
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<List<DictionaryEntry>> = query
        .flatMapLatest(searchDictionary::invoke)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(value: String) { query.value = value }
}
