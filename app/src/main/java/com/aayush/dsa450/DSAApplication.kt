package com.aayush.dsa450

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import com.aayush.dsa450.data.initialization.DataInitializationService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DSAApplication : Application(), Configuration.Provider {
    
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var dataInitializationService: DataInitializationService
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.d("DSA Application started")
        
        // Initialize data in background
        applicationScope.launch {
            dataInitializationService.initializeDataIfNeeded()
        }
    }
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
