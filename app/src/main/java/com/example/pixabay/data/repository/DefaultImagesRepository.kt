package com.example.pixabay.data.repository

import com.example.pixabay.domain.repository.ImagesRepository
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class DefaultImagesRepository @Inject constructor(
    // todo api, dao
) : ImagesRepository {

    private val fakeCachedQueries = mutableSetOf("")

    override suspend fun getCachedImagesList(query: String): List<String> {
        if (!fakeCachedQueries.contains(query)) {
            error("cache error")
        }
        return listOf("Cached") + query.toCharArray().toList().map { it.toString() }
    }

    override suspend fun getFreshImagesList(query: String): List<String> {
        delay(1000)
        if (Random.nextBoolean()) {
            error("network error")
        }
        fakeCachedQueries.add(query)
        Timber.d("Cached queries: $fakeCachedQueries")
        return listOf("Fresh") + query.toCharArray().toList().map { it.toString() }
    }

    override suspend fun cacheImagesList(images: List<String>) {
        // todo save to dao
    }

    override suspend fun getImageDetails(id: String): String {
        return "$id Details"
    }
}