package com.chiniyar.app.core.model

/** Result returned by any translation engine, local or remote. */
data class Translation(
    val source: String,
    val translated: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val confidence: Float? = null
)
