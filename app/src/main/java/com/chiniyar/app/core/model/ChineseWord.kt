package com.chiniyar.app.core.model

/** A dictionary-ready Chinese lexical entry. */
data class ChineseWord(
    val simplified: String,
    val traditional: String? = null,
    val pinyin: String,
    val meanings: List<String>,
    val example: String? = null,
    val exampleMeaning: String? = null,
    val hskLevel: Int? = null
)
