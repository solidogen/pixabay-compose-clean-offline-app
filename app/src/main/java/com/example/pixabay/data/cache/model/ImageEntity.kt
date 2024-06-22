package com.example.pixabay.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "tags")
    val tags: String,
    @ColumnInfo(name = "thumbnailUrl")
    val thumbnailUrl: String,
    @ColumnInfo(name = "largeImageUrl")
    val largeImageUrl: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "downloads")
    val downloads: Int,
    @ColumnInfo(name = "comments")
    val comments: Int,
    @ColumnInfo(name = "query")
    val query: String,
)
