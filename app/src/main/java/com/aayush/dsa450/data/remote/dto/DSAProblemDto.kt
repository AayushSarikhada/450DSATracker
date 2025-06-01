package com.aayush.dsa450.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.aayush.dsa450.domain.model.DSAProblem

@JsonClass(generateAdapter = true)
data class DSAProblemDto(
    @Json(name = "id") val id: Int,
    @Json(name = "problemType") val problemType: String,
    @Json(name = "problemName") val problemName: String,
    @Json(name = "problemUrl") val problemUrl: String = "",
    @Json(name = "problemDone") val problemDone: Boolean,
    @Json(name = "difficulty") val difficulty: String = "MEDIUM",
    @Json(name = "lastUpdated") val lastUpdated: Long = System.currentTimeMillis()
)

fun DSAProblemDto.toDomain(): DSAProblem {
    return DSAProblem(
        id = id,
        problemType = problemType,
        problemName = problemName,
        problemUrl = problemUrl,
        isDone = problemDone,
        difficulty = try {
            DSAProblem.Difficulty.valueOf(difficulty.uppercase())
        } catch (e: IllegalArgumentException) {
            DSAProblem.Difficulty.MEDIUM
        },
        lastUpdated = lastUpdated
    )
}

fun DSAProblem.toDto(): DSAProblemDto {
    return DSAProblemDto(
        id = id,
        problemType = problemType,
        problemName = problemName,
        problemUrl = problemUrl,
        problemDone = isDone,
        difficulty = difficulty.name,
        lastUpdated = lastUpdated
    )
}
