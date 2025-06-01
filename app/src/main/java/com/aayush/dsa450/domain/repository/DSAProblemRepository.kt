package com.aayush.dsa450.domain.repository

import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface DSAProblemRepository {
    
    fun getAllProblems(): Flow<Resource<List<DSAProblem>>>
    
    fun getProblemById(id: Int): Flow<Resource<DSAProblem>>
    
    fun getProblemsByType(type: String): Flow<Resource<List<DSAProblem>>>
    
    suspend fun updateProblemStatus(problemId: Int, isDone: Boolean): Resource<Unit>
    
    suspend fun syncWithRemote(): Resource<Unit>
    
    suspend fun importProblemsFromCsv(): Resource<Unit>
    
    fun getCompletedProblemsCount(): Flow<Int>
    
    fun getTotalProblemsCount(): Flow<Int>
    
    fun getProgressPercentage(): Flow<Float>
}
