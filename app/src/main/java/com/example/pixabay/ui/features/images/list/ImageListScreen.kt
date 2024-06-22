package com.example.pixabay.ui.features.images.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val state by viewModel.images.collectAsState(DataState.loading())
        Text(text = "Images list screen")
        VerticalSpace(padding = 16.dp)

        Button(onClick = {
            viewModel.setSearchQuery(listOf("Ala", "Kot", "Pat", "Mat").random())
        }) {
            Text(text = "Random query")
        }
        state.data?.let {
            ImageList(images = it, isLoading = state.isLoading)
        }
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
private fun ImageList(images: List<ImageModel>, isLoading: Boolean) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.alpha(if (isLoading) 0.5f else 1f)
    ) {
        items(images) { item->
            AsyncImage(
                model = item.thumbnailUrl,
                contentDescription = item.tags,
            )
        }
    }
}