package com.example.pixabay.ui.features.images.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.usecase.GetImageByIdUseCase
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.testutils.BaseUiTest
import com.example.pixabay.ui.navigation.Destination
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
class ImageDetailsScreenTest : BaseUiTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    private val getImageByIdUseCase: GetImageByIdUseCase = mockk()
    private lateinit var viewModel: ImageDetailsViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        hiltRule.inject()
        savedStateHandle = SavedStateHandle()
        viewModel = ImageDetailsViewModel(savedStateHandle, getImageByIdUseCase)
    }

    @Test
    fun imageDetailsScreen_displaysLoadingIndicatorInitially() = runTest {
        coEvery { getImageByIdUseCase.execute(any()) } returns flowOf(DataState.loading())
        savedStateHandle[Destination.Images.ID_PLACEHOLDER_NO_BRACKETS] = "1"
        composeTestRule.setContent {
            val navController = rememberNavController()
            ImageDetailsScreen(viewModel = viewModel, goBack = { navController.popBackStack() })
        }
        composeTestRule.onNodeWithTag(TestTags.LOADING_INDICATOR)
            .assertIsDisplayed()
    }

    @Test
    fun imageDetailsScreen_displaysImageDetailsWhenSuccessful() = runTest {
        val image = ImageModel("1", "user1", "tags", "thumb", "large", 1, 2, 3)
        coEvery { getImageByIdUseCase.execute(image.id) } returns flowOf(DataState.success(image))
        savedStateHandle[Destination.Images.ID_PLACEHOLDER_NO_BRACKETS] = image.id
        composeTestRule.setContent {
            val navController = rememberNavController()
            ImageDetailsScreen(viewModel = viewModel, goBack = { navController.popBackStack() })
        }
        composeTestRule.onNodeWithText("By " + image.username).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.downloads, image.downloads)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.likes, image.likes)).assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.comments, image.comments)).assertIsDisplayed()
    }

    @Test
    fun imageDetailsScreen_displaysErrorStateOnError() = runTest {
        coEvery { getImageByIdUseCase.execute(any()) } returns flowOf(
            DataState.error(
                DataError.ErrorMessage("Test Error")
            )
        )
        savedStateHandle[Destination.Images.ID_PLACEHOLDER_NO_BRACKETS] = "1"
        composeTestRule.setContent {
            val navController = rememberNavController()
            ImageDetailsScreen(viewModel = viewModel, goBack = { navController.popBackStack() })
        }
        composeTestRule.onNodeWithText("Test Error").assertIsDisplayed()
    }

    @Test
    fun imageDetailsScreen_backButtonNavigatesBack() {
        val image = ImageModel("1","user1", "tags", "thumb", "large", 1, 2, 3)
        coEvery { getImageByIdUseCase.execute(image.id) } returns flowOf(DataState.success(image))
        savedStateHandle[Destination.Images.ID_PLACEHOLDER_NO_BRACKETS] = image.id
        composeTestRule.setContent {
            val navController = rememberNavController()
            ImageDetailsScreen(viewModel = viewModel, goBack = { navController.popBackStack() })
        }
        composeTestRule.onNodeWithContentDescription("Go back").assertIsDisplayed()
            .performClick()
    }
}
