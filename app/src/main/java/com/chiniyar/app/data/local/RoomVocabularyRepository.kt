package com.chiniyar.app.data.local

import kotlinx.coroutines.flow.Flow

/** Persistent repository backed by Android's built-in SQLite database. */
class RoomVocabularyRepository(private val database: VocabularyDatabase) {
    val words: Flow<List<VocabularyEntry>> = database.observeAll()

    suspend fun add(entry: VocabularyEntry) {
        database.add(entry)
    }

    suspend fun remove(entry: VocabularyEntry) {
        database.remove(entry.word)
    }

    suspend fun contains(word: String): Boolean = database.contains(word)
}
