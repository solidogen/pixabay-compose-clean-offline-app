package com.example.pixabay.domain.usecase

import com.example.pixabay.data.utils.runCatchingAsync
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.repository.ImagesRepository
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * First emits cached images for given query if available, then fetches fresh images and emits again
 * */
@Singleton
class GetImageListUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    fun execute(query: String): Flow<DataState<List<ImageModel>>> = flow {
        try {
            // Cached
            val cachedImagesResult = runCatchingAsync {
                imagesRepository.getCachedImagesList(query = query)
            }
            val cachedImages = cachedImagesResult.getOrNull().orEmpty()
            if (cachedImagesResult.isSuccess) {
                emit(DataState.success(cachedImages))
            } else {
                Timber.e(cachedImagesResult.exceptionOrNull())
                emit(DataState.loading(cachedImages))
            }

            // Fresh
            val freshImagesResult = runCatchingAsync {
                imagesRepository.getFreshImagesList(query = query)
            }
            val freshImages = freshImagesResult.getOrNull().orEmpty()
            if (freshImagesResult.isSuccess) {
                imagesRepository.cacheImagesList(images = freshImages, query = query)
                emit(DataState.success(freshImages))
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
            emit(DataState.error(DataError.UnknownError))
        }
    }
}
