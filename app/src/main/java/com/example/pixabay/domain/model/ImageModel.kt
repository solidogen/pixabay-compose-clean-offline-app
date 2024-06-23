package com.example.pixabay.domain.model

data class ImageModel(
    val id: String,
    val username: String,
    val tagsString: String,
    val thumbnailUrl: String,
    val largeImageUrl: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
) {
    val tagList: List<String> = tagsString.split(",").map { it.trim() }
}