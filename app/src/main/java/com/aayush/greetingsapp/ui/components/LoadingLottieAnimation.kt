package com.aayush.greetingsapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.aayush.greetingsapp.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingLottieAnimation(modifier: Modifier) {

    val loadingComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading
        )
    )

    LottieAnimation(
        composition = loadingComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        modifier = modifier
    )

}