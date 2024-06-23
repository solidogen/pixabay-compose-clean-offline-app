package com.example.pixabay.data.utils

import com.example.pixabay.data.cache.model.ImageEntity
import com.example.pixabay.data.network.model.ImageDto
import com.example.pixabay.domain.model.ImageModel

fun ImageDto.mapToDomain(): ImageModel = ImageModel(
    id = id,
    username = username,
    tagsString = tags,
    thumbnailUrl = thumbnailUrl,
    largeImageUrl = largeImageUrl,
    likes = likes,
    downloads = downloads,
    comments = comments
)

fun ImageEntity.mapToDomain(): ImageModel = ImageModel(
    id = id,
    username = username,
    tagsString = tags,
    thumbnailUrl = thumbnailUrl,
    largeImageUrl = largeImageUrl,
    likes = likes,
    downloads = downloads,
    comments = comments
)

fun ImageModel.mapToEntity(query: String): ImageEntity = ImageEntity(
    id = id,
    username = username,
    tags = tagsString,
    thumbnailUrl = thumbnailUrl,
    largeImageUrl = largeImageUrl,
    likes = likes,
    downloads = downloads,
    comments = comments,
    query = query
)
