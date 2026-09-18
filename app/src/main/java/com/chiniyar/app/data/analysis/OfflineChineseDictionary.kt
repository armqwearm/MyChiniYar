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
            entries = context.applicationContext.assets.open(ASSET_PATH)
                .bufferedReader()
                .useLines(OfflineDictionaryParser::parse)
        }
    }

    fun meaning(word: String): String? = entries[word]

    fun contains(word: String): Boolean = entries.containsKey(word)

    fun words(): Set<String> = entries.keys
}
