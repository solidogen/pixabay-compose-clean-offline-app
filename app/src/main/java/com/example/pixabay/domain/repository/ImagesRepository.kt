package com.example.pixabay.domain.repository

interface ImagesRepository {

    // todo null? errors?
    suspend fun getCachedImagesList(query: String): List<String>
    suspend fun getFreshImagesList(query: String): List<String>
    suspend fun cacheImagesList(images: List<String>)
    suspend fun getImageDetails(id: String): String
}