package com.chiniyar.app.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomVocabularyRepository(private val dao: VocabularyDao) {
    val words: Flow<List<VocabularyEntry>> = dao.observeAll().map { list ->
        list.map { VocabularyEntry(it.word, it.pinyin, it.meaning, it.createdAt) }
    }

    suspend fun add(entry: VocabularyEntry) {
        dao.insert(VocabularyEntity(entry.word.trim(), entry.pinyin, entry.meaning, entry.createdAt))
    }

    suspend fun remove(entry: VocabularyEntry) {
        dao.delete(VocabularyEntity(entry.word, entry.pinyin, entry.meaning, entry.createdAt))
    }
}
