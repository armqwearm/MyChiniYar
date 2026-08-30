package com.chiniyar.app.data.analysis

/** Pure parser for the bundled TSV dictionary. Kept Android-free so it can be unit tested. */
object OfflineDictionaryParser {
    fun parse(lines: Sequence<String>): Map<String, String> = buildMap {
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
