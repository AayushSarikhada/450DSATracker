package com.aayush.dsa450.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aayush.dsa450.domain.model.DSAProblem

@Entity(tableName = "dsa_problems")
data class DSAProblemEntity(
    @PrimaryKey val id: Int,
    val problemType: String,
    val problemName: String,
    val problemUrl: String,
    val isDone: Boolean,
    val difficulty: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun DSAProblemEntity.toDomain(): DSAProblem {
    return DSAProblem(
        id = id,
        problemType = problemType,
        problemName = problemName,
        problemUrl = problemUrl,
        isDone = isDone,
        difficulty = DSAProblem.Difficulty.valueOf(difficulty),
        lastUpdated = lastUpdated
    )
}

fun DSAProblem.toEntity(): DSAProblemEntity {
    return DSAProblemEntity(
        id = id,
        problemType = problemType,
        problemName = problemName,
        problemUrl = problemUrl,
        isDone = isDone,
        difficulty = difficulty.name,
        lastUpdated = lastUpdated
    )
}
