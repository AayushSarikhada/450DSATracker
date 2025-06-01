package com.aayush.dsa450.data.initialization

import com.aayush.dsa450.data.local.dao.DSAProblemDao
import com.aayush.dsa450.data.local.datasource.CsvDataSource
import com.aayush.dsa450.data.local.entity.toEntity
import com.aayush.dsa450.data.remote.datasource.FirebaseDataSource
import com.aayush.dsa450.data.remote.dto.toDto
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializationService @Inject constructor(
    private val csvDataSource: CsvDataSource,
    private val localDao: DSAProblemDao,
    private val firebaseDataSource: FirebaseDataSource
) {
    
    suspend fun initializeDataIfNeeded() {
        try {
            // Check if local database is empty
            val localProblems = localDao.getAllProblems().first()
            
            if (localProblems.isEmpty()) {
                Timber.d("Local database is empty, initializing from CSV...")
                
                // Load problems from CSV
                val csvProblems = csvDataSource.parseProblemsFromCsv()
                
                if (csvProblems.isNotEmpty()) {
                    // Insert into local database
                    localDao.insertProblems(csvProblems.map { it.toEntity() })
                    Timber.d("Inserted ${csvProblems.size} problems into local database")
                    
                    // Sync to Firebase
                    val result = firebaseDataSource.syncProblems(csvProblems.map { it.toDto() })
                    if (result.isSuccess) {
                        Timber.d("Successfully synced ${csvProblems.size} problems to Firebase")
                    } else {
                        Timber.w("Failed to sync to Firebase: ${result.exceptionOrNull()?.message}")
                    }
                } else {
                    Timber.w("No problems found in CSV file")
                }
            } else {
                Timber.d("Local database already contains ${localProblems.size} problems")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error during data initialization")
        }
    }
}
