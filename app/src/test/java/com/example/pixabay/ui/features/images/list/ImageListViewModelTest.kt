package com.example.pixabay.ui.features.images.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageListUseCase
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.features.images.list.ImageListViewModel.Companion.DEFAULT_SEARCH_QUERY
import com.example.pixabay.ui.features.images.list.ImageListViewModel.Companion.SEARCH_QUERY_KEY
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ImageListViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private val getImageListUseCase: GetImageListUseCase = mockk()
    private lateinit var viewModel: ImageListViewModel

    @Before
    @OptIn(ExperimentalCoroutinesApi::class)
    fun setup() {
        savedStateHandle = SavedStateHandle()
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ImageListViewModel(savedStateHandle, getImageListUseCase)
    }

    @Test
    fun `initial state is loading with default query`() = runTest {
        coEvery { getImageListUseCase.execute(DEFAULT_SEARCH_QUERY) } returns flowOf(
            DataState.loading()
        )

        viewModel.imagesState.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            assertThat(initialState.data).isNull()
            assertThat(initialState.error).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits success state when use case returns images`() = runTest {
        val query = "cats"
        val images = listOf(
            ImageModel("1", "user1", "tags","thumb", "large", 1, 2, 3),
            ImageModel("2", "user2", "tags", "thumb", "large", 4, 5, 6)
        )
        savedStateHandle[SEARCH_QUERY_KEY] = query
        coEvery { getImageListUseCase.execute(query) } returns flowOf(
            DataState.loading(),
            DataState.success(images)
        )

        viewModel.imagesState.test {
            assertThat(awaitItem().isLoading).isTrue() // Initial loading state
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.data).isEqualTo(images)
            assertThat(successState.error).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits error state when use case returns error`() = runTest {
        val query = "dogs"
        val error = DataError.UnknownError
        savedStateHandle[SEARCH_QUERY_KEY] = query
        coEvery { getImageListUseCase.execute(query) } returns flowOf(
            DataState.loading(),
            DataState.error(error)
        )

        viewModel.imagesState.test {
            assertThat(awaitItem().isLoading).isTrue() // Initial loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.data).isNull()
            assertThat(errorState.error).isEqualTo(error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setSearchQuery updates query in SavedStateHandle`() = runTest {
        val newQuery = "flowers"
        viewModel.setSearchQuery(newQuery)
        assertThat(savedStateHandle.get<String>(SEARCH_QUERY_KEY)).isEqualTo(newQuery)
    }

    @Test
    fun `retrySearch triggers a retry with the same query`() = runTest {
        val initialQuery = "sunsets"
        coEvery { getImageListUseCase.execute(any()) } returns flowOf(
            DataState.loading(),
            DataState.error(DataError.UnknownError)
        )
        savedStateHandle[SEARCH_QUERY_KEY] = initialQuery

        viewModel.imagesState.test {// Should emit loading state twice (initial and after retry)
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().error).isEqualTo(DataError.UnknownError)
            cancelAndIgnoreRemainingEvents()
        }

        val images = listOf(
            ImageModel("1", "user1", "tags","thumb", "large", 1, 2, 3),
            ImageModel("2", "user2", "tags", "thumb", "large", 4, 5, 6)
        )
        coEvery { getImageListUseCase.execute(any()) } returns flowOf(
            DataState.loading(),
            DataState.success(images)
        )
        viewModel.retrySearch()

        viewModel.imagesState.test {// Should emit loading state twice (initial and after retry)
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().data).isEqualTo(images)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
