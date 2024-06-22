package com.example.pixabay.domain.repository

import com.example.pixabay.domain.model.ImageModel

interface ImagesRepository {
    suspend fun getCachedImagesList(query: String): List<ImageModel>
    suspend fun getFreshImagesList(query: String): List<ImageModel>
    suspend fun cacheImagesList(images: List<ImageModel>, query: String)
    suspend fun getImageById(id: String): ImageModel?
}