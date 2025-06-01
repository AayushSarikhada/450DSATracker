package com.aayush.dsa450.di

import android.content.Context
import androidx.room.Room
import com.aayush.dsa450.data.local.dao.DSAProblemDao
import com.aayush.dsa450.data.local.database.DSADatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DSADatabase {
        return Room.databaseBuilder(
            context,
            DSADatabase::class.java,
            DSADatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideDSAProblemDao(database: DSADatabase): DSAProblemDao {
        return database.dsaProblemDao()
    }
}
