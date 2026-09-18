package com.chiniyar.app.domain.repository

import com.chiniyar.app.core.model.DictionaryEntry
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun search(query: String): Flow<List<DictionaryEntry>>
}
