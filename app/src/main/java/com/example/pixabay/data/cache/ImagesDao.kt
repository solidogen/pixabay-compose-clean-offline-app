package com.example.pixabay.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.pixabay.data.cache.model.ImageEntity

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceImages(images: List<ImageEntity>)

    @Transaction
    @Query("SELECT * FROM images WHERE images.`query` = :query ")
    suspend fun getImagesByQuery(query: String): List<ImageEntity>

    @Transaction
    @Query("SELECT * FROM images WHERE images.id = :id ")
    suspend fun getImageById(id: String): ImageEntity?
}