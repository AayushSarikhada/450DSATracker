package com.aayush.dsa450.data.local.database

import androidx.room.TypeConverter

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Long? {
        return value
    }

}
