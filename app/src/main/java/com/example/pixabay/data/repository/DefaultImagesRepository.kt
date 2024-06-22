package com.example.pixabay.data.repository

import com.example.pixabay.data.cache.AppDatabase
import com.example.pixabay.data.cache.ImagesDao
import com.example.pixabay.data.network.ImagesApi
import com.example.pixabay.data.utils.bodyOrThrow
import com.example.pixabay.data.utils.mapToDomain
import com.example.pixabay.data.utils.mapToEntity
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.repository.ImagesRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class DefaultImagesRepository @Inject constructor(
    private val imagesApi: ImagesApi,
    private val appDatabase: AppDatabase,
    private val imagesDao: ImagesDao = appDatabase.imagesDao()
) : ImagesRepository {

    override suspend fun getCachedImagesList(query: String): List<ImageModel> {
        val cachedImages = imagesDao.getImagesByQuery(query = query)
        if (cachedImages.isEmpty()) {
            error("No images cached for query: $query")
        }
        return cachedImages.map { it.mapToDomain() }
    }

    override suspend fun getFreshImagesList(query: String): List<ImageModel> {
        delay(1000)
        // todo remove this
//        if (Random.nextBoolean()) {
//            error("network error")
//        }
        return imagesApi.searchImages(query = query).bodyOrThrow().map { it.mapToDomain() }
    }

    override suspend fun cacheImagesList(images: List<ImageModel>, query: String) {
        val imageEntities = images.map { it.mapToEntity(query = query) }
        imagesDao.insertOrReplaceImages(imageEntities)
    }

    override suspend fun getImageDetails(id: String): String {
        return "$id Details"
    }
}