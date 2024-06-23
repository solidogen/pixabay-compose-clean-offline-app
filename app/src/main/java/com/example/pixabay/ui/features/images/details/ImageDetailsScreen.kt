package com.example.pixabay.ui.features.images.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.ImageComposable

@Composable
fun ImageDetailsScreen(
    viewModel: ImageDetailsViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val state by viewModel.state.collectAsState(DataState.loading())
        val image = state.data

        image?.let {
            ImageComposable(
                image = image,
                url = { it.largeImageUrl },
            )
        }

        state.error?.let {
            ErrorState(error = it, onRetry = {})
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}
