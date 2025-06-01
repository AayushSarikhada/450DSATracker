package com.aayush.dsa450.domain.usecase

import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProblemsUseCase @Inject constructor(
    private val repository: DSAProblemRepository
) {
    operator fun invoke(): Flow<Resource<List<DSAProblem>>> {
        return repository.getAllProblems()
    }
}
