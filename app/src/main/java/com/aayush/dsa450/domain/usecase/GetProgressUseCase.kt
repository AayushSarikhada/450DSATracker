package com.aayush.dsa450.domain.usecase

import com.aayush.dsa450.domain.repository.DSAProblemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProgressUseCase @Inject constructor(
    private val repository: DSAProblemRepository
) {
    fun getCompletedCount(): Flow<Int> = repository.getCompletedProblemsCount()
    
    fun getTotalCount(): Flow<Int> = repository.getTotalProblemsCount()
    
    fun getProgressPercentage(): Flow<Float> = repository.getProgressPercentage()
}
