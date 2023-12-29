package com.aayush.greetingsapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aayush.greetingsapp.ui.screens.DSAProblemListScreen
import com.aayush.greetingsapp.ui.theme.GreetingsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingsAppTheme {
                DSAProblemListScreen()
            }
        }
    }

}