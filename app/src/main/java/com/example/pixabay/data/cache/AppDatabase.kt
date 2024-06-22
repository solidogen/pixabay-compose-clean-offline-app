package com.example.pixabay.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixabay.data.cache.model.ImageEntity

@Database(
    entities = [ImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
}