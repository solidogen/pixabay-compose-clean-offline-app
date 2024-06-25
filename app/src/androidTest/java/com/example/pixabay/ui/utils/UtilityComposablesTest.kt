package com.example.pixabay.ui.utils

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.pixabay.R
import com.example.pixabay.domain.utils.DataError
import com.example.pixabay.testutils.BaseUiTest
import org.junit.Rule
import org.junit.Test

class UtilityComposablesTest : BaseUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadableAsyncImage_displaysErrorState() {
        composeTestRule.setContent {
            LoadableAsyncImage(
                model = "invalid_url", // Simulate error state
                contentDescription = "Error Image"
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.unknown_error)).assertIsDisplayed()
    }

    @Test
    fun imageSearchBar_displaysSearchIconAndPlaceholder() {
        composeTestRule.setContent {
            ImageSearchBar(query = "", onQueryChange = {})
        }

        composeTestRule.onNodeWithText(getString(R.string.search))
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessageAndRetryButton() {
        composeTestRule.setContent {
            ErrorState(error = DataError.ErrorMessage("Test Error"), onRetry = {})
        }

        composeTestRule.onNodeWithText("Test Error").assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.retry))
            .assertIsDisplayed()
    }
}