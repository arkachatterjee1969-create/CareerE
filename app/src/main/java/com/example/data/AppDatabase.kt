package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ResumeEntity::class], version = 1, exportSchema = false)
@TypeConverters(ResumeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
}
