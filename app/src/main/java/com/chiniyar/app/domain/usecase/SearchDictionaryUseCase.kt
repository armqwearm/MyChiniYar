package com.chiniyar.app.domain.usecase

import com.chiniyar.app.core.model.DictionaryEntry
import com.chiniyar.app.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow

class SearchDictionaryUseCase(private val repository: DictionaryRepository) {
    operator fun invoke(query: String): Flow<List<DictionaryEntry>> = repository.search(query)
}
