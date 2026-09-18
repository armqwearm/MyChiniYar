package com.chiniyar.app.data.local

/** A vocabulary item saved by the user. */
data class VocabularyEntry(
    val word: String,
    val pinyin: String,
    val meaning: String,
    val createdAt: Long = System.currentTimeMillis()
)
