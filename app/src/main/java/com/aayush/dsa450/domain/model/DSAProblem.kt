package com.aayush.dsa450.domain.model

data class DSAProblem(
    val id: Int,
    val problemType: String,
    val problemName: String,
    val problemUrl: String,
    val isDone: Boolean,
    val difficulty: Difficulty,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    enum class Difficulty {
        EASY, MEDIUM, HARD
    }
}
