package com.aayush.dsa450.data.repository

import com.aayush.dsa450.data.local.dao.DSAProblemDao
import com.aayush.dsa450.data.local.datasource.CsvDataSource
import com.aayush.dsa450.data.local.entity.toDomain
import com.aayush.dsa450.data.local.entity.toEntity
import com.aayush.dsa450.data.remote.datasource.FirebaseDataSource
import com.aayush.dsa450.data.remote.dto.toDomain
import com.aayush.dsa450.data.remote.dto.toDto
import com.aayush.dsa450.domain.model.DSAProblem
import com.aayush.dsa450.domain.model.Resource
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DSAProblemRepositoryImpl @Inject constructor(
    private val localDao: DSAProblemDao,
    private val firebaseDataSource: FirebaseDataSource,
    private val csvDataSource: CsvDataSource
) : DSAProblemRepository {
    
    override fun getAllProblems(): Flow<Resource<List<DSAProblem>>> = flow {
        emit(Resource.Loading())
        
        try {
            // First emit local data
            localDao.getAllProblems()
                .map { entities -> entities.map { it.toDomain() } }
                .catch { e ->
                    Timber.e(e, "Error fetching local problems")
                    emit(Resource.Error("Failed to load local data: ${e.message}"))
                }
                .collect { localProblems ->
                    if (localProblems.isNotEmpty()) {
                        emit(Resource.Success(localProblems))
                    }
                }
            
            // Then sync with Firebase
            firebaseDataSource.getAllProblems()
                .catch { e ->
                    Timber.e(e, "Error fetching remote problems")
                    // Don't emit error if we have local data
                }
                .collect { remoteDtos ->
                    val remoteProblems = remoteDtos.map { it.toDomain() }
                    
                    // Update local database
                    localDao.insertProblems(remoteProblems.map { it.toEntity() })
                    
                    emit(Resource.Success(remoteProblems))
                }
        } catch (e: Exception) {
            Timber.e(e, "Error in getAllProblems")
            emit(Resource.Error("Failed to load problems: ${e.message}"))
        }
    }
    
    override fun getProblemById(id: Int): Flow<Resource<DSAProblem>> = flow {
        emit(Resource.Loading())
        
        try {
            localDao.getProblemById(id)
                .catch { e ->
                    Timber.e(e, "Error fetching problem by id: $id")
                    emit(Resource.Error("Failed to load problem: ${e.message}"))
                }
                .collect { entity ->
                    if (entity != null) {
                        emit(Resource.Success(entity.toDomain()))
                    } else {
                        emit(Resource.Error("Problem not found"))
                    }
                }
        } catch (e: Exception) {
            Timber.e(e, "Error in getProblemById")
            emit(Resource.Error("Failed to load problem: ${e.message}"))
        }
    }
    
    override fun getProblemsByType(type: String): Flow<Resource<List<DSAProblem>>> = flow {
        emit(Resource.Loading())
        
        try {
            localDao.getProblemsByType(type)
                .map { entities -> entities.map { it.toDomain() } }
                .catch { e ->
                    Timber.e(e, "Error fetching problems by type: $type")
                    emit(Resource.Error("Failed to load problems: ${e.message}"))
                }
                .collect { problems ->
                    emit(Resource.Success(problems))
                }
        } catch (e: Exception) {
            Timber.e(e, "Error in getProblemsByType")
            emit(Resource.Error("Failed to load problems: ${e.message}"))
        }
    }
    
    override suspend fun updateProblemStatus(problemId: Int, isDone: Boolean): Resource<Unit> {
        return try {
            val timestamp = System.currentTimeMillis()
            
            // Update local first
            localDao.updateProblemStatus(problemId, isDone, timestamp)
            
            // Then sync with Firebase
            val result = firebaseDataSource.updateProblemStatus(problemId, isDone)
            
            if (result.isSuccess) {
                Resource.Success(Unit)
            } else {
                Timber.e(result.exceptionOrNull(), "Failed to sync with Firebase")
                Resource.Error("Update saved locally, will sync when connection is available")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating problem status")
            Resource.Error("Failed to update problem: ${e.message}")
        }
    }
    
    override suspend fun syncWithRemote(): Resource<Unit> {
        return try {
            val localProblems = localDao.getAllProblems()
            // Convert to DTOs and sync
            // Implementation would depend on sync strategy
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error syncing with remote")
            Resource.Error("Sync failed: ${e.message}")
        }
    }
    
    override suspend fun importProblemsFromCsv(): Resource<Unit> {
        return try {
            val problems = csvDataSource.parseProblemsFromCsv()
            
            if (problems.isNotEmpty()) {
                localDao.insertProblems(problems.map { it.toEntity() })
                
                // Optionally sync to Firebase
                val result = firebaseDataSource.syncProblems(problems.map { it.toDto() })
                
                if (result.isSuccess) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("CSV imported locally, Firebase sync failed")
                }
            } else {
                Resource.Error("No problems found in CSV")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error importing from CSV")
            Resource.Error("Failed to import CSV: ${e.message}")
        }
    }
    
    override fun getCompletedProblemsCount(): Flow<Int> = localDao.getCompletedProblemsCount()
    
    override fun getTotalProblemsCount(): Flow<Int> = localDao.getTotalProblemsCount()
    
    override fun getProgressPercentage(): Flow<Float> = 
        combine(getCompletedProblemsCount(), getTotalProblemsCount()) { completed, total ->
            if (total > 0) (completed.toFloat() / total.toFloat()) * 100f else 0f
        }
}
