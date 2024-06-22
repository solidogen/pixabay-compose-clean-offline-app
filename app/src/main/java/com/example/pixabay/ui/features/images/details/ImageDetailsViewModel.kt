package com.example.pixabay.ui.features.images.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pixabay.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // todo private val getImageDetailsUseCase: GetImageDetailsUseCase
) : ViewModel()  {

    // todo debounce z m.p.

    private val idState: StateFlow<String> = MutableStateFlow(
        savedStateHandle.get<String>(Destination.Images.ID_PLACEHOLDER_NO_BRACKETS).orEmpty()
    )

    init {
        idState.value.let { Timber.d("IdState: $it") }
    }

    fun getId(): String = idState.value
}