package com.example.pixabay.di

import android.content.Context
import androidx.room.Room
import com.example.pixabay.data.cache.AppDatabase
import com.example.pixabay.data.cache.ImagesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideImagesDao(database: AppDatabase): ImagesDao {
        return database.imagesDao()
    }
}