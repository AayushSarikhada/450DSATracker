# Firebase Integration Guide

## Current Status
The DSA 450 app has been refactored with Clean Architecture and includes stubbed Firebase integration. All Firebase dependencies and plugins are temporarily commented out to ensure compilation in environments without proper Android SDK configuration.

## What's Already Prepared

### 1. Firebase Data Source (Stubbed)
- Location: `app/src/main/java/com/aayush/dsa450/data/remote/datasource/FirebaseDataSource.kt`
- Status: ✅ Properly stubbed with all required methods
- Ready for: Firebase Firestore implementation

### 2. Firebase Module (Stubbed)
- Location: `app/src/main/java/com/aayush/dsa450/di/FirebaseModule.kt`
- Status: ✅ Empty module ready for dependency injection providers
- Ready for: Firebase service providers

### 3. Repository Integration
- Location: `app/src/main/java/com/aayush/dsa450/data/repository/DSAProblemRepositoryImpl.kt`
- Status: ✅ Already injecting and using FirebaseDataSource
- Ready for: Real Firebase implementation

### 4. Build Configuration
- Location: `app/build.gradle.kts`
- Status: ✅ All Firebase dependencies commented and ready to uncomment
- Ready for: Uncommenting Firebase dependencies

## Steps to Complete Firebase Integration

### Step 1: Enable Firebase Dependencies
In `app/build.gradle.kts`, uncomment these lines:
```kotlin
// Plugins
alias(libs.plugins.google.services)
alias(libs.plugins.firebase.crashlytics)

// Dependencies
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.analytics)
implementation(libs.firebase.firestore)
implementation(libs.firebase.crashlytics)
```

### Step 2: Verify Firebase Configuration
- Ensure `google-services.json` is in the `app/` directory
- Verify the file contains correct project configuration

### Step 3: Implement FirebaseDataSource
Replace the stubbed implementation in `FirebaseDataSource.kt`:

```kotlin
@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val problemsCollection = firestore.collection("problems")
    
    fun getAllProblems(): Flow<List<DSAProblemDto>> = callbackFlow {
        val listenerRegistration = problemsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val problems = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<DSAProblemDto>()
                } ?: emptyList()
                
                trySend(problems)
            }
        
        awaitClose { listenerRegistration.remove() }
    }
    
    suspend fun updateProblemStatus(problemId: Int, isDone: Boolean): Result<Unit> {
        return try {
            problemsCollection.document(problemId.toString())
                .update("problemDone", isDone)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun syncProblems(problems: List<DSAProblemDto>): Result<Unit> {
        return try {
            val batch = firestore.batch()
            problems.forEach { problem ->
                val docRef = problemsCollection.document(problem.id.toString())
                batch.set(docRef, problem)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Step 4: Implement Firebase Module
Add Firebase providers to `FirebaseModule.kt`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
    
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return Firebase.analytics
    }
}
```

### Step 5: Update Imports
Add necessary imports to files that use Firebase:

```kotlin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
```

## Version Catalog Entries
The `libs.versions.toml` file already includes these Firebase dependencies:

```toml
[versions]
firebase-bom = "32.8.1"

[libraries]
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebase-bom" }
firebase-analytics = { group = "com.google.firebase", name = "firebase-analytics-ktx" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore-ktx" }
firebase-crashlytics = { group = "com.google.firebase", name = "firebase-crashlytics-ktx" }

[plugins]
google-services = { id = "com.google.gms.google-services", version = "4.4.1" }
firebase-crashlytics = { id = "com.google.firebase.crashlytics", version = "2.9.9" }
```

## Testing Firebase Integration

### Unit Tests
The existing tests will continue to work as they mock the repository.

### Integration Tests
Add Firebase integration tests in `app/src/androidTest/`:

```kotlin
@HiltAndroidTest
class FirebaseIntegrationTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var firebaseDataSource: FirebaseDataSource
    
    // Test Firebase operations
}
```

## Current Advantages
✅ **Clean Architecture**: Firebase is properly isolated in the data layer
✅ **Dependency Injection**: Ready for easy Firebase service injection
✅ **Repository Pattern**: Firebase integration won't affect other layers
✅ **Error Handling**: Proper Result/Resource patterns already in place
✅ **Testing**: Existing tests will continue to work with mocked data

## Migration Path
1. The app currently works with local Room database
2. Firebase can be enabled gradually without breaking existing functionality
3. The repository handles both local and remote data sources seamlessly
4. Background sync via WorkManager is already implemented

This architecture ensures a smooth Firebase integration without disrupting the existing functionality.
