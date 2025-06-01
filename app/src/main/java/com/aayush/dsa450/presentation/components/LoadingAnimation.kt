package com.aayush.dsa450.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aayush.dsa450.R

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    size: Int = 120
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )
    
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        modifier = modifier.size(size.dp)
    )
}
