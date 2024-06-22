package com.example.pixabay.domain.repository

import com.example.pixabay.domain.model.ImageModel

interface ImagesRepository {

    // todo null? errors?
    suspend fun getCachedImagesList(query: String): List<ImageModel>
    suspend fun getFreshImagesList(query: String): List<ImageModel>
    suspend fun cacheImagesList(images: List<ImageModel>, query: String)
    suspend fun getImageDetails(id: String): String
}