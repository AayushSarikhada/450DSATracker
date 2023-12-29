package com.aayush.greetingsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aayush.greetingsapp.ui.components.DSAProblemList
import com.aayush.greetingsapp.ui.components.LoadingLottieAnimation
import com.aayush.greetingsapp.ui.utils.SetStatusBarColor
import com.aayush.greetingsapp.viewmodel.DSAProblemVM

@Composable
fun DSAProblemListScreen(
    viewModel: DSAProblemVM = DSAProblemVM()
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            if (viewModel.isLoading.value) {
                LoadingLottieAnimation(modifier = Modifier.align(Alignment.Center))
            } else {
                SetStatusBarColor(color = Color.Green)
                DSAProblemList(viewModel.dsaProblems, viewModel)
            }
        }

    }
}