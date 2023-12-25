package com.aayush.greetingsapp.model

data class DSAProblem(
    val id: Int = 0,
    val problemType: String = "N/A",
    val problemName: String = "N/A",
    var problemDone: Boolean = false
)
