package com.chiniyar.app.data.analysis

data class AnalyzedWord(
    val word: String,
    val pinyin: String,
    val meaning: String,
    val saved: Boolean = false
)
