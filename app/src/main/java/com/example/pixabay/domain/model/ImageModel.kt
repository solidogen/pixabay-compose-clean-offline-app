package com.example.pixabay.domain.model

import com.example.pixabay.ui.utils.LARGE_IMAGE_CACHE_KEY_AFFIX
import com.example.pixabay.ui.utils.THUMBNAIL_CACHE_KEY_AFFIX

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

    fun getCacheKey(isThumbnail: Boolean): String =
        if (isThumbnail) {
            id + THUMBNAIL_CACHE_KEY_AFFIX
        } else {
            id + LARGE_IMAGE_CACHE_KEY_AFFIX
        }

    fun getImageUrl(isThumbnail: Boolean): String =
        if (isThumbnail) {
            thumbnailUrl
        } else {
            largeImageUrl
        }
}
