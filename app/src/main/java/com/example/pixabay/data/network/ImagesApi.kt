package com.example.pixabay.data.network

import com.example.pixabay.data.network.model.ImageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("api")
    suspend fun searchImages(
        @Query(value = "q", encoded = true) query: String
    ): Response<List<ImageDto>>
}