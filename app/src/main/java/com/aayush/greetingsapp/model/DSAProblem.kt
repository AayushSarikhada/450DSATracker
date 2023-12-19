package com.aayush.greetingsapp.model

data class DSAProblem(
    val id: Int = 0,
    val problemType: String,
    val problemName: String,
    var problemDone: Boolean
)
