package com.example.pixabay.ui.features.images.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageByIdUseCase
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ImageDetailsViewModelTest {

    private lateinit var savedStateHandle: SavedStateHandle
    private val getImageByIdUseCase: GetImageByIdUseCase = mockk()
    private lateinit var viewModel: ImageDetailsViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle()
        viewModel = ImageDetailsViewModel(savedStateHandle, getImageByIdUseCase)
    }

    @Test
    fun `initial state is loading when ID is not available`() = runTest {
        coEvery { getImageByIdUseCase.execute(any()) } returns flowOf(DataState.loading())
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()
            assertThat(initialState.data).isNull()
            assertThat(initialState.error).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits success state when use case returns image`() = runTest {
        val imageId = "123"
        val image = ImageModel(imageId, "user", "tags", "thumb", "large", 1, 2, 3)
        savedStateHandle["id"] = imageId // Set the ID in SavedStateHandle
        coEvery { getImageByIdUseCase.execute(imageId) } returns flowOf(
            DataState.loading(),
            DataState.success(image)
        )

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isTrue() // Initial loading state
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.data).isEqualTo(image)
            assertThat(successState.error).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits error state when use case returns error`() = runTest {
        val imageId = "456"
        val error = DataError.UnknownError
        savedStateHandle["id"] = imageId // Set the ID in SavedStateHandle
        coEvery { getImageByIdUseCase.execute(imageId) } returns flowOf(
            DataState.loading(),
            DataState.error(error)
        )

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isTrue() // Initial loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.data).isNull()
            assertThat(errorState.error).isEqualTo(error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}