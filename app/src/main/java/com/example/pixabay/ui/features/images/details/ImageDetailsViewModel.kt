package com.example.pixabay.ui.features.images.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageByIdUseCase
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ImageDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getImageByIdUseCase: GetImageByIdUseCase
) : ViewModel()  {

    val state: Flow<DataState<ImageModel>> =
        savedStateHandle.getStateFlow(Destination.Images.ID_PLACEHOLDER_NO_BRACKETS, "")
            .flatMapLatest { getImageByIdUseCase.execute(it) }
}