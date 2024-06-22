package com.example.pixabay.domain.model

data class ImageModel(
    val id: String,
    val username: String,
    val tags: String,
    val thumbnailUrl: String,
    val largeImageUrl: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)
