package com.example.pixabay.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "user")
    val username: String,
    @Json(name = "tags")
    val tags: String,
    @Json(name = "previewURL") // todo - change to webformatURL for quality?
    val thumbnailUrl: String
)
