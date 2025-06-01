package com.aayush.dsa450.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aayush.dsa450.presentation.problemlist.ProblemListScreen
import com.aayush.dsa450.presentation.settings.SettingsScreen

@Composable
fun DSANavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "problem_list"
    ) {
        composable("problem_list") {
            ProblemListScreen(navController = navController)
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
