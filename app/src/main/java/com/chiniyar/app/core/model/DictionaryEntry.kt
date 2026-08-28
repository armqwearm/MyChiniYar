package com.chiniyar.app.core.model

data class DictionaryEntry(
    val hanzi: String,
    val pinyin: String,
    val meaningFa: String,
    val partOfSpeech: String? = null,
    val example: String? = null
)
