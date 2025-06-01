package com.aayush.dsa450.integration

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aayush.dsa450.data.local.database.DSADatabase
import com.aayush.dsa450.data.local.entity.DSAProblemEntity
import com.aayush.dsa450.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DSAAppIntegrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var database: DSADatabase

    @Before
    fun setup() {
        hiltRule.inject()
        
        // Insert test data
        runBlocking {
            val testProblems = listOf(
                DSAProblemEntity(
                    id = 1,
                    problemType = "Array",
                    problemName = "Two Sum",
                    problemUrl = "https://leetcode.com/problems/two-sum/",
                    isDone = false,
                    difficulty = "EASY"
                ),
                DSAProblemEntity(
                    id = 2,
                    problemType = "Array",
                    problemName = "Best Time to Buy and Sell Stock",
                    problemUrl = "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/",
                    isDone = true,
                    difficulty = "EASY"
                )
            )
            database.dsaProblemDao().insertProblems(testProblems)
        }
    }

    @Test
    fun appDisplaysProblemsCorrectly() {
        // Verify app title is displayed
        composeTestRule.onNodeWithText("DSA 450").assertIsDisplayed()
        
        // Verify test problems are displayed
        composeTestRule.onNodeWithText("Two Sum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Best Time to Buy and Sell Stock").assertIsDisplayed()
    }

    @Test
    fun userCanToggleProblemStatus() {
        // Find and click on the Two Sum problem
        composeTestRule.onNodeWithText("Two Sum").performClick()
        
        // Verify status change (this would need to be implemented based on UI feedback)
        // For example, check if a completion indicator appears
    }

    @Test
    fun searchFunctionalityWorksCorrectly() {
        // Click search icon
        composeTestRule.onNodeWithContentDescription("Search").performClick()
        
        // Search functionality test would go here
        // This depends on the search implementation
    }

    @Test
    fun loadingStateIsDisplayedCorrectly() {
        // This test would verify loading states
        // Implementation depends on how loading is handled in the UI
    }

    @Test
    fun errorStateIsHandledCorrectly() {
        // This test would verify error handling
        // Implementation depends on error state UI
    }
}
