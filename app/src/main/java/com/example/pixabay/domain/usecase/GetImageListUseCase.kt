package com.example.pixabay.domain.usecase

import com.example.pixabay.domain.repository.ImagesRepository
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetImageListUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    fun execute(query: String): Flow<DataState<List<String>>> = flow {
        try {
            emit(DataState.loading())

            // Cache
            val cachedImagesResult = runCatching {
                imagesRepository.getCachedImagesList(query = query)
            }
            val cachedImages = cachedImagesResult.getOrNull().orEmpty()
            if (cachedImagesResult.isSuccess) {
                Timber.d("GetImageListUseCase: getCachedImagesList success")
                emit(DataState.success(cachedImages))
            } else {
                Timber.e("GetImageListUseCase: getCachedImagesList failed")
            }

            // Fresh
            val freshImagesResult = runCatching {
                imagesRepository.getFreshImagesList(query = query)
            }
            val freshImages = freshImagesResult.getOrNull().orEmpty()
            if (freshImagesResult.isSuccess) {
                Timber.d("GetImageListUseCase: getFreshImagesList success")
                imagesRepository.cacheImagesList(freshImages)
                emit(DataState.success(freshImages))
            } else {
                Timber.e("GetImageListUseCase: getFreshImagesList failed")
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
            Timber.e("GetImageListUseCase: error", e)
            emit(DataState.error(DataError.UnknownError, cachedData = null))
        }
    }
}
