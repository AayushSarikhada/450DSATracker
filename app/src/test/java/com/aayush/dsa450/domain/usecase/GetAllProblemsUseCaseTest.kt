package com.aayush.dsa450.domain.usecase

import app.cash.turbine.test
import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllProblemsUseCaseTest {

    private lateinit var repository: DSAProblemRepository
    private lateinit var useCase: GetAllProblemsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAllProblemsUseCase(repository)
    }

    @Test
    fun `getAllProblems should return success when repository returns success`() = runTest {
        // Given
        val mockProblems = listOf(
            DSAProblem(
                id = 1,
                problemType = "Array",
                problemName = "Test Problem",
                problemUrl = "https://test.com",
                isDone = false,
                difficulty = DSAProblem.Difficulty.EASY
            )
        )
        every { repository.getAllProblems() } returns flowOf(Resource.Success(mockProblems))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result is Resource.Success)
            assertEquals(mockProblems, result.data)
            awaitComplete()
        }
    }

    @Test
    fun `getAllProblems should return error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Network error"
        every { repository.getAllProblems() } returns flowOf(Resource.Error(errorMessage))

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result is Resource.Error)
            assertEquals(errorMessage, result.message)
            awaitComplete()
        }
    }

    @Test
    fun `getAllProblems should return loading when repository returns loading`() = runTest {
        // Given
        every { repository.getAllProblems() } returns flowOf(Resource.Loading())

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result is Resource.Loading)
            awaitComplete()
        }
    }
}
