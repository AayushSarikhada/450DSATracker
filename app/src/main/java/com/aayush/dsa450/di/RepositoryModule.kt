package com.aayush.dsa450.di

import com.aayush.dsa450.data.repository.DSAProblemRepositoryImpl
import com.aayush.dsa450.domain.repository.DSAProblemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindDSAProblemRepository(
        dsaProblemRepositoryImpl: DSAProblemRepositoryImpl
    ): DSAProblemRepository
}
