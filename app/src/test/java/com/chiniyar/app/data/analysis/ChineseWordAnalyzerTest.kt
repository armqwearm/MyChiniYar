package com.chiniyar.app.data.analysis

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChineseWordAnalyzerTest {
    private val analyzer = ChineseWordAnalyzer()

    @Test
    fun segmentation_is_unique_and_limited_to_20() {
        val words = analyzer.segment("我喜欢学习中文我喜欢学习中文今天去学校")
        assertTrue(words.size <= 20)
        assertEquals(words.size, words.distinct().size)
        assertTrue(words.contains("喜欢"))
        assertTrue(words.contains("学习"))
        assertTrue(words.contains("中文"))
    }

    @Test
    fun pinyin_is_generated_locally() {
        assertTrue(analyzer.pinyin("中文").isNotBlank())
    }
}
