package com.chiniyar.app.domain

import com.chiniyar.app.data.repository.InMemoryDictionaryRepository
import com.chiniyar.app.domain.usecase.SearchDictionaryUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchDictionaryUseCaseTest {
    private val useCase = SearchDictionaryUseCase(InMemoryDictionaryRepository())

    @Test
    fun emptyQueryReturnsDictionary() = runTest {
        val result = useCase("").first()
        assertTrue(result.size >= 5)
    }

    @Test
    fun hanziSearchFindsExpectedEntry() = runTest {
        val result = useCase("北京").first()
        assertEquals("Běijīng", result.single().pinyin)
    }

    @Test
    fun persianMeaningSearchFindsChineseWord() = runTest {
        val result = useCase("سلام").first()
        assertEquals("你好", result.single().hanzi)
    }
}
