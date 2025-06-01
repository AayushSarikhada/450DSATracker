package com.aayush.dsa450.data.remote.datasource

import com.aayush.dsa450.data.remote.dto.DSAProblemDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val problemsCollection = firestore.collection("problems")
    
    fun getAllProblems(): Flow<List<DSAProblemDto>> = callbackFlow {
        Timber.d("Firebase data source - setting up real-time listener")
        
        val listenerRegistration = problemsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Error listening to problems collection")
                    close(error)
                    return@addSnapshotListener
                }
                
                val problems = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject<DSAProblemDto>()?.copy(id = doc.id.toIntOrNull() ?: 0)
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing document: ${doc.id}")
                        null
                    }
                } ?: emptyList()
                
                Timber.d("Firebase data source - received ${problems.size} problems")
                trySend(problems)
            }
        
        awaitClose { 
            Timber.d("Firebase data source - removing listener")
            listenerRegistration.remove() 
        }
    }
    
    suspend fun updateProblemStatus(problemId: Int, isDone: Boolean): Result<Unit> {
        return try {
            Timber.d("Firebase data source - updating problem $problemId to $isDone")
            problemsCollection.document(problemId.toString())
                .update("problemDone", isDone)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Firebase data source - failed to update problem status")
            Result.failure(e)
        }
    }
    
    suspend fun syncProblems(problems: List<DSAProblemDto>): Result<Unit> {
        return try {
            Timber.d("Firebase data source - syncing ${problems.size} problems")
            val batch = firestore.batch()
            problems.forEach { problem ->
                val docRef = problemsCollection.document(problem.id.toString())
                batch.set(docRef, problem)
            }
            batch.commit().await()
            Timber.d("Firebase data source - sync completed successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Firebase data source - sync failed")
            Result.failure(e)
        }
    }
}
