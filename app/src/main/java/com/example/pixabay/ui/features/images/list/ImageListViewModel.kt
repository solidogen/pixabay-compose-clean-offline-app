package com.example.pixabay.ui.features.images.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageListUseCase
import com.example.pixabay.domain.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ImageListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getImageListUseCase: GetImageListUseCase
) : ViewModel() {

    private var lastSetQuery: String = DEFAULT_SEARCH_QUERY

    val query: StateFlow<String> =
        savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, DEFAULT_SEARCH_QUERY)

    val imagesState: Flow<DataState<List<ImageModel>>> = query
        .debounce { query ->
            if (query == lastSetQuery) {
                0L
            } else {
                SEARCH_DEBOUNCE_MS
            }
        }
        .flatMapLatest { query ->
            lastSetQuery = query
            getImageListUseCase.execute(query)
        }

    fun setSearchQuery(query: String) {
        Timber.d("Setting query to $query")
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

    fun retrySearch() {
        viewModelScope.launch {
            val query = savedStateHandle.get<String>(SEARCH_QUERY_KEY)
            savedStateHandle[SEARCH_QUERY_KEY] = ""
            delay(RETRY_DELAY_MS)
            savedStateHandle[SEARCH_QUERY_KEY] = query
        }
    }

    private companion object {
        const val DEFAULT_SEARCH_QUERY = "fruits"
        const val SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY"
        const val RETRY_DELAY_MS = 10L // lets savedStateHandle emit empty string first
        const val SEARCH_DEBOUNCE_MS = 500L
    }
}