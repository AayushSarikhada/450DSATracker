package com.aayush.dsa450.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DSAProblemRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("Starting background sync...")
            
            val result = repository.syncWithRemote()
            
            when {
                result.data != null -> {
                    Timber.d("Background sync completed successfully")
                    Result.success()
                }
                else -> {
                    Timber.e("Background sync failed: ${result.message}")
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Background sync failed with exception")
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "sync_problems_work"
    }
}
