package com.example.pixabay.domain.usecase

import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.repository.ImagesRepository
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetImageListUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    fun execute(query: String): Flow<DataState<List<ImageModel>>> = flow {
        try {
            emit(DataState.loading())

            // Cached
            val cachedImagesResult = runCatching {
                imagesRepository.getCachedImagesList(query = query)
            }
            val cachedImages = cachedImagesResult.getOrNull().orEmpty()
            if (cachedImagesResult.isSuccess) {
                emit(DataState.success(cachedImages))
            } else {
                Timber.e(cachedImagesResult.exceptionOrNull())
            }

            // Fresh
            val freshImagesResult = runCatching {
                imagesRepository.getFreshImagesList(query = query)
            }
            val freshImages = freshImagesResult.getOrNull().orEmpty()
            if (freshImagesResult.isSuccess) {
                /**
                 * Reducing recompositions, since every time we fetch a image from
                 * pixabay, image urls are generated from scratch, although image stays the same
                 *
                 * Emitting only actual changes to the list
                 * */
                val updatedImages = freshImages.map { freshImage ->
                    val correspondingCachedImage = cachedImages.find { it.id == freshImage.id }
                    if (correspondingCachedImage != null) {
                        freshImage.copy(
                            thumbnailUrl = correspondingCachedImage.thumbnailUrl,
                            largeImageUrl = correspondingCachedImage.largeImageUrl
                        )
                    } else {
                        freshImage
                    }
                }
                imagesRepository.cacheImagesList(images = updatedImages, query = query)
                emit(DataState.success(updatedImages))
            } else {
                Timber.e(freshImagesResult.exceptionOrNull())
                emit(
                    DataState.error(
                        error = DataError.ErrorMessage(
                            freshImagesResult.exceptionOrNull()?.message.orEmpty()
                        ),
                        cachedData = cachedImages
                    )
                )
            }
        } catch (e: Throwable) {
            Timber.e(e)
            emit(DataState.error(DataError.UnknownError, cachedData = null))
        }
    }
}
