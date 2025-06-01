package com.aayush.dsa450.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aayush.dsa450.data.local.dao.DSAProblemDao
import com.aayush.dsa450.data.local.entity.DSAProblemEntity

@Database(
    entities = [DSAProblemEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DSADatabase : RoomDatabase() {
    
    abstract fun dsaProblemDao(): DSAProblemDao
    
    companion object {
        const val DATABASE_NAME = "dsa_database"
    }
}
