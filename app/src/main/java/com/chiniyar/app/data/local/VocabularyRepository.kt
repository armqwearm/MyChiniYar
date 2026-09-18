package com.chiniyar.app.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** In-memory vocabulary store for the first offline vocabulary-bank iteration. */
class VocabularyRepository {
    private val entries = LinkedHashMap<String, VocabularyEntry>()
    private val _words = MutableStateFlow<List<VocabularyEntry>>(emptyList())
    val words: StateFlow<List<VocabularyEntry>> = _words.asStateFlow()

    fun add(entry: VocabularyEntry) {
        val key = entry.word.trim()
        if (key.isEmpty()) return
        entries[key] = entry.copy(word = key)
        _words.value = entries.values.toList()
    }

    fun remove(word: String) {
        entries.remove(word.trim())
        _words.value = entries.values.toList()
    }

    fun contains(word: String): Boolean = entries.containsKey(word.trim())
}
