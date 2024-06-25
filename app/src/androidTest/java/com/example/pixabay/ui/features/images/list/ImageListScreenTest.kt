package com.example.pixabay.ui.features.images.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageListUseCase
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.testutils.BaseUiTest
import com.example.pixabay.ui.features.images.list.ImageListViewModel.Companion.DEFAULT_SEARCH_QUERY
import com.example.pixabay.ui.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ImageListScreenTest : BaseUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    private val getImageListUseCase: GetImageListUseCase = mockk()
    private lateinit var viewModel: ImageListViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        hiltRule.inject()
        savedStateHandle = SavedStateHandle()
        viewModel = ImageListViewModel(savedStateHandle, getImageListUseCase)
    }

    @Test
    fun imageListScreen_displaysLoadingIndicatorInitially() = runTest {
        coEvery { getImageListUseCase.execute(DEFAULT_SEARCH_QUERY) } returns flowOf(DataState.loading())
        composeTestRule.setContent {
            ImageListScreen(viewModel = viewModel, goToImageDetailsScreen = {})
        }
        composeTestRule.onNodeWithTag(TestTags.LOADING_INDICATOR)
            .assertIsDisplayed()
    }

    @Test
    fun imageListScreen_displaysImagesWhenSuccessful() = runTest {
        val images = listOf(
            ImageModel("1", "user1", "tags", "thumb", "large", 1, 2, 3),
            ImageModel("2", "user2", "tags", "thumb", "large", 4, 5, 6)
        )
        coEvery { getImageListUseCase.execute(any()) } returns flowOf(DataState.success(images))
        composeTestRule.setContent {
            ImageListScreen(viewModel = viewModel, goToImageDetailsScreen = {})
        }
        // Wait for the images to load
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(TestTags.IMAGE_ITEM).fetchSemanticsNodes().size == images.size
        }

        images.forEach { image ->
            composeTestRule.onNodeWithText("By " + image.username).assertIsDisplayed()
        }
    }

    @Test
    fun imageListScreen_displaysErrorStateOnError() = runTest {
        coEvery { getImageListUseCase.execute(DEFAULT_SEARCH_QUERY) } returns flowOf(
            DataState.error(
                DataError.ErrorMessage("Test Error")
            )
        )
        composeTestRule.setContent {
            ImageListScreen(viewModel = viewModel, goToImageDetailsScreen = {})
        }
        composeTestRule.onNodeWithText("Test Error").assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.retry))
            .assertIsDisplayed()
    }

    @Test
    fun imageListScreen_clickingImageOpensDialog() {
        val images = listOf(
            ImageModel("1", "user1", "tags", "thumb", "large", 1, 2, 3)
        )
        coEvery { getImageListUseCase.execute(DEFAULT_SEARCH_QUERY)} returns flowOf(DataState.success(images))

        composeTestRule.setContent {
            ImageListScreen(viewModel = viewModel, goToImageDetailsScreen = {})
        }
        // Wait for the images to load
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(TestTags.IMAGE_ITEM).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag(TestTags.IMAGE_ITEM, useUnmergedTree = true)
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.liking_the_image))
            .assertIsDisplayed()
    }
}
