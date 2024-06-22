package com.example.pixabay.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchImagesResponse(
    @Json(name = "hits")
    val images: List<ImageDto>
)
