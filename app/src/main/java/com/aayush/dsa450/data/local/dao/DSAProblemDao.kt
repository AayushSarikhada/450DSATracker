package com.aayush.dsa450.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aayush.dsa450.data.local.entity.DSAProblemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DSAProblemDao {
    
    @Query("SELECT * FROM dsa_problems ORDER BY id ASC")
    fun getAllProblems(): Flow<List<DSAProblemEntity>>
    
    @Query("SELECT * FROM dsa_problems WHERE id = :id")
    fun getProblemById(id: Int): Flow<DSAProblemEntity?>
    
    @Query("SELECT * FROM dsa_problems WHERE problemType = :type ORDER BY id ASC")
    fun getProblemsByType(type: String): Flow<List<DSAProblemEntity>>
    
    @Query("UPDATE dsa_problems SET isDone = :isDone, lastUpdated = :timestamp WHERE id = :id")
    suspend fun updateProblemStatus(id: Int, isDone: Boolean, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM dsa_problems WHERE isDone = 1")
    fun getCompletedProblemsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM dsa_problems")
    fun getTotalProblemsCount(): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblems(problems: List<DSAProblemEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProblem(problem: DSAProblemEntity)
    
    @Update
    suspend fun updateProblem(problem: DSAProblemEntity)
    
    @Query("DELETE FROM dsa_problems")
    suspend fun deleteAllProblems()
    
    @Query("SELECT * FROM dsa_problems WHERE lastUpdated > :timestamp")
    suspend fun getProblemsUpdatedAfter(timestamp: Long): List<DSAProblemEntity>
}
