package com.chiniyar.app.data.analysis

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineDictionaryParserTest {
    @Test
    fun parses_valid_tsv_and_ignores_comments_and_malformed_lines() {
        val dictionary = OfflineDictionaryParser.parse(
            sequenceOf(
                "# comment",
                "你好\tسلام",
                "学习\tیادگیری",
                "malformed",
                "\tmissing-word",
                "中文\t"
            )
        )

        assertEquals(2, dictionary.size)
        assertEquals("سلام", dictionary["你好"])
        assertEquals("یادگیری", dictionary["学习"])
        assertFalse(dictionary.containsKey("malformed"))
    }

    @Test
    fun duplicate_words_use_last_valid_definition() {
        val dictionary = OfflineDictionaryParser.parse(
            sequenceOf("你好\tسلام", "你好\tدرود")
        )
        assertEquals(1, dictionary.size)
        assertEquals("درود", dictionary["你好"])
    }

    @Test
    fun trims_entries() {
        val dictionary = OfflineDictionaryParser.parse(
            sequenceOf("  中国  \t  چین  ")
        )
        assertTrue(dictionary.containsKey("中国"))
        assertEquals("چین", dictionary["中国"])
    }
}
