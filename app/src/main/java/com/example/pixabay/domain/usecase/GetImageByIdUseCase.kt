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
class GetImageByIdUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository
) {

    fun execute(id: String): Flow<DataState<ImageModel>> = flow {
        try {
            emit(DataState.loading())
            val image = imagesRepository.getImageById(id = id)
            if (image != null) {
                emit(DataState.success(image))
            } else {
                emit(DataState.error(DataError.UnknownError, cachedData = null))
            }
        } catch (e: Throwable) {
            Timber.e(e)
            emit(DataState.error(DataError.UnknownError, cachedData = null))
        }
    }
}