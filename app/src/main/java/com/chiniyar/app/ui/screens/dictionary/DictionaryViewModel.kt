package com.chiniyar.app.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiniyar.app.core.model.DictionaryEntry
import com.chiniyar.app.domain.usecase.SearchDictionaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DictionaryViewModel(
    private val searchDictionary: SearchDictionaryUseCase
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val entries: StateFlow<List<DictionaryEntry>> = _query
        .flatMapLatest(searchDictionary::invoke)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(value: String) { _query.value = value }
}
