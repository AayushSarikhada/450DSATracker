package com.aayush.dsa450.domain.usecase

import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import javax.inject.Inject

class UpdateProblemStatusUseCase @Inject constructor(
    private val repository: DSAProblemRepository
) {
    suspend operator fun invoke(problemId: Int, isDone: Boolean): Resource<Unit> {
        return repository.updateProblemStatus(problemId, isDone)
    }
}
