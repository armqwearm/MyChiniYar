package com.chiniyar.app.data.analysis

import net.sourceforge.pinyin4j.PinyinHelper

/**
 * Offline longest-match segmenter backed by the bundled Chinese dictionary.
 * Unknown Han characters are retained as single-character candidates.
 * The returned list is ordered by first appearance and contains at most 20 unique items.
 */
class ChineseWordAnalyzer {
    private val lexicon: List<String> = OfflineChineseDictionary.words()
        .filter { it.isNotEmpty() }
        .sortedByDescending { it.length }

    fun segment(text: String): List<String> {
        val result = LinkedHashSet<String>()
        val normalized = text.trim()
        if (normalized.isEmpty()) return emptyList()

        var index = 0
        while (index < normalized.length && result.size < MAX_WORDS) {
            val char = normalized[index]
            if (!isChinese(char)) {
                index++
                continue
            }

            var match: String? = null
            for (candidate in lexicon) {
                if (candidate.length > normalized.length - index) continue
                if (normalized.regionMatches(index, candidate, 0, candidate.length)) {
                    match = candidate
                    break
                }
            }

            if (match == null) {
                match = char.toString()
            }

            result.add(match)
            index += match.length
        }
        return result.take(MAX_WORDS)
    }

    /** Returns numeric-tone pinyin, e.g. 学习 -> xue2 xi2. */
    fun pinyin(word: String): String = buildString {
        word.forEachIndexed { index, char ->
            if (index > 0) append(' ')
            append(PinyinHelper.toHanyuPinyinStringArray(char)?.firstOrNull() ?: char)
        }
    }

    private fun isChinese(char: Char): Boolean =
        char in '\u3400'..'\u4DBF' || char in '\u4E00'..'\u9FFF'

    companion object {
        private const val MAX_WORDS = 20
    }
}
