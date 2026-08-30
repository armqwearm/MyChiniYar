package com.chiniyar.app.data.analysis

import net.sourceforge.pinyin4j.PinyinHelper

/**
 * Offline longest-match segmenter backed by the bundled Chinese dictionary.
 * Unknown Han characters are retained as single-character candidates instead of being lost.
 */
class ChineseWordAnalyzer {
    private val lexicon: Set<String> = OfflineChineseDictionary.words()
        .plus("买东西")

    fun segment(text: String): List<String> {
        val result = LinkedHashSet<String>()
        val runs = text.split(Regex("[^\\u3400-\\u4DBF\\u4E00-\\u9FFF]+"))

        for (run in runs) {
            if (run.isEmpty()) continue
            var index = 0
            while (index < run.length && result.size < 20) {
                var match: String? = null
                val maxLength = minOf(6, run.length - index)
                for (length in maxLength downTo 2) {
                    val candidate = run.substring(index, index + length)
                    if (candidate in lexicon) {
                        match = candidate
                        break
                    }
                }
                if (match == null) match = run[index].toString()
                result += match
                index += match.length
            }
            if (result.size >= 20) break
        }
        return result.take(20)
    }

    /** Returns numeric-tone pinyin, e.g. 学习 -> xue2 xi2. */
    fun pinyin(word: String): String = buildString {
        word.forEachIndexed { index, char ->
            if (index > 0) append(' ')
            append(PinyinHelper.toHanyuPinyinStringArray(char)?.firstOrNull() ?: char)
        }
    }
}
