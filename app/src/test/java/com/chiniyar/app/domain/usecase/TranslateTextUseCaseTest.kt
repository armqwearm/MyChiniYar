package com.chiniyar.app.domain.usecase

import com.chiniyar.app.core.common.AppResult
import com.chiniyar.app.core.model.Language
import com.chiniyar.app.core.model.Translation
import com.chiniyar.app.domain.repository.TranslationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TranslateTextUseCaseTest {
    @Test
    fun `empty input returns validation error`() = runTest {
        val useCase = TranslateTextUseCase(FakeRepository())
        val result = useCase("   ", Language.CHINESE, Language.PERSIAN)
        assert(result is AppResult.Error)
    }

    @Test
    fun `same language returns input without repository call`() = runTest {
        val repository = FakeRepository()
        val useCase = TranslateTextUseCase(repository)
        val result = useCase("你好", Language.CHINESE, Language.CHINESE)
        assertEquals("你好", (result as AppResult.Success).data.translated)
        assertEquals(0, repository.calls)
    }

    private class FakeRepository : TranslationRepository {
        var calls = 0

        override suspend fun translate(
            text: String,
            source: Language,
            target: Language
        ): AppResult<Translation> {
            calls++
            return AppResult.Success(Translation(text, "سلام", source, target))
        }
    }
}
