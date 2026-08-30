package com.chiniyar.app.data.local

import kotlinx.coroutines.flow.Flow

/** Persistent repository backed by Android's built-in SQLite database. */
class RoomVocabularyRepository(private val database: VocabularyDatabase) {
    val words: Flow<List<VocabularyEntry>> = database.words

    suspend fun add(entry: VocabularyEntry): Boolean = database.add(entry)

    suspend fun remove(entry: VocabularyEntry): Boolean = database.remove(entry.word)

    suspend fun contains(word: String): Boolean = database.contains(word)
}
