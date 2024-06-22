package com.example.pixabay.di

import com.example.pixabay.data.repository.DefaultImagesRepository
import com.example.pixabay.domain.repository.ImagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindImagesRepository(repo: DefaultImagesRepository): ImagesRepository
}