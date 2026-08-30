package com.chiniyar.app.data.analysis

import android.content.Context

/** Loads the offline Chinese-Persian lexicon from a versioned app asset. */
object OfflineChineseDictionary {
    private const val ASSET_PATH = "dictionary/zh_fa_dictionary.tsv"

    @Volatile
    private var entries: Map<String, String> = emptyMap()

    fun initialize(context: Context) {
        if (entries.isNotEmpty()) return
        synchronized(this) {
            if (entries.isNotEmpty()) return
            val loaded = context.applicationContext.assets.open(ASSET_PATH).bufferedReader().useLines { lines ->
                buildMap {
                    lines.forEach { line ->
                        if (line.isBlank() || line.startsWith("#")) return@forEach
                        val separator = line.indexOf('\t')
                        if (separator <= 0 || separator >= line.lastIndex) return@forEach
                        val word = line.substring(0, separator).trim()
                        val meaning = line.substring(separator + 1).trim()
                        if (word.isNotEmpty() && meaning.isNotEmpty()) put(word, meaning)
                    }
                }
            }
            entries = loaded
        }
    }

    fun meaning(word: String): String? = entries[word]

    fun contains(word: String): Boolean = entries.containsKey(word)

    fun words(): Set<String> = entries.keys
}
