package com.aayush.dsa450.presentation.problemlist

import app.cash.turbine.test
import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.usecase.GetAllProblemsUseCase
import com.aayush.dsa450.domain.usecase.GetProgressUseCase
import com.aayush.dsa450.domain.usecase.UpdateProblemStatusUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProblemListViewModelTest {

    private lateinit var getAllProblemsUseCase: GetAllProblemsUseCase
    private lateinit var updateProblemStatusUseCase: UpdateProblemStatusUseCase
    private lateinit var getProgressUseCase: GetProgressUseCase
    private lateinit var viewModel: ProblemListViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        getAllProblemsUseCase = mockk()
        updateProblemStatusUseCase = mockk()
        getProgressUseCase = mockk()

        every { getProgressUseCase.getCompletedCount() } returns flowOf(5)
        every { getProgressUseCase.getTotalCount() } returns flowOf(10)
        every { getProgressUseCase.getProgressPercentage() } returns flowOf(50f)
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given
        every { getAllProblemsUseCase() } returns flowOf(Resource.Loading())

        // When
        viewModel = ProblemListViewModel(getAllProblemsUseCase, updateProblemStatusUseCase, getProgressUseCase)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
        }
    }

    @Test
    fun `should update state when problems are loaded successfully`() = runTest {
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
        every { getAllProblemsUseCase() } returns flowOf(Resource.Success(mockProblems))

        // When
        viewModel = ProblemListViewModel(getAllProblemsUseCase, updateProblemStatusUseCase, getProgressUseCase)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(mockProblems, state.problems)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `should filter problems by search query`() = runTest {
        // Given
        val mockProblems = listOf(
            DSAProblem(1, "Array", "Array Problem", "", false, DSAProblem.Difficulty.EASY),
            DSAProblem(2, "String", "String Problem", "", false, DSAProblem.Difficulty.MEDIUM)
        )
        every { getAllProblemsUseCase() } returns flowOf(Resource.Success(mockProblems))

        viewModel = ProblemListViewModel(getAllProblemsUseCase, updateProblemStatusUseCase, getProgressUseCase)

        // When
        viewModel.onEvent(ProblemListEvent.SearchProblems("Array"))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Array", state.searchQuery)
            assertEquals(1, state.filteredProblems.size)
            assertEquals("Array Problem", state.filteredProblems.first().problemName)
        }
    }

    @Test
    fun `should update problem status successfully`() = runTest {
        // Given
        val mockProblems = listOf(
            DSAProblem(1, "Array", "Test Problem", "", false, DSAProblem.Difficulty.EASY)
        )
        every { getAllProblemsUseCase() } returns flowOf(Resource.Success(mockProblems))
        coEvery { updateProblemStatusUseCase(1, true) } returns Resource.Success(Unit)

        viewModel = ProblemListViewModel(getAllProblemsUseCase, updateProblemStatusUseCase, getProgressUseCase)

        // When
        viewModel.onEvent(ProblemListEvent.UpdateProblemStatus(1, true))

        // Then - Should not show error
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(null, state.error)
        }
    }
}
