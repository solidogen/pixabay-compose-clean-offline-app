package com.example.pixabay.ui.features.images.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.VerticalSpace

@Composable
fun ImageListScreen(
    viewModel: ImageListViewModel,
    goToImageDetailsScreen: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val state by viewModel.state.collectAsState(DataState.loading())

        // TODO - is this still needed?
        val images by remember {
            derivedStateOf { state.data }
        }

        Button(onClick = {
            viewModel.setSearchQuery(listOf("Ala", "Kot", "Pat", "Mat").random())
        }) {
            Text(text = "Random query")
        }
        VerticalSpace(padding = 16.dp)

        ImageList(
            images = images.orEmpty(),
            goToImageDetailsScreen = goToImageDetailsScreen
        )

        state.error?.let {
            ErrorState(error = it, onRetry = viewModel::retrySearch)
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        Button(onClick = { goToImageDetailsScreen.invoke("1234") }) {
            Text(text = "Go to image details screen, 1234")
        }
    }
}

@Composable
private fun ImageList(
    images: List<ImageModel>,
    goToImageDetailsScreen: (String) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
    ) {
        items(
            items = images,
            key = { it.id }
        ) { image ->
            AsyncImage(
                model = image.thumbnailUrl,
                contentDescription = image.tags,
                modifier = Modifier.clickable {
                    goToImageDetailsScreen.invoke(image.id)
                }
            )
        }
    }
}