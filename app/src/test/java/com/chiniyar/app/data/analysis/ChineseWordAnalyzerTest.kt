package com.chiniyar.app.data.analysis

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChineseWordAnalyzerTest {
    private val analyzer = ChineseWordAnalyzer()

    @Test
    fun segmentation_is_unique_and_limited_to_40() {
        val source = buildString {
            repeat(10) { append("我喜欢学习中文今天去学校") }
            append("中国朋友老师学生手机电脑天气价格")
        }
        val words = analyzer.segment(source)
        assertTrue(words.size <= 40)
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
