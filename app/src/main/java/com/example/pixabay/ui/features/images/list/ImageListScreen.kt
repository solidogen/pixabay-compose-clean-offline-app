package com.example.pixabay.ui.features.images.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pixabay.R
import com.example.pixabay.data.utils.ComposeImmutableList
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.ImageComposable
import com.example.pixabay.ui.utils.LoadingIndicator
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

        Button(onClick = {
            viewModel.setSearchQuery(listOf("Ala", "Kot", "Pat", "Mat").random())
        }) {
            Text(text = "Random query")
        }
        VerticalSpace(padding = 16.dp)

        state.data?.let { images ->
            ImageList(
                images = ComposeImmutableList.from(images),
                goToImageDetailsScreen = goToImageDetailsScreen
            )
        }
        state.error?.let {
            ErrorState(error = it, onRetry = viewModel::retrySearch)
        }
        if (state.isLoading) {
            LoadingIndicator()
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(if (isLandscape) 3 else 2),
        modifier = Modifier.scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        items(
            items = images,
            key = { it.id }
        ) { image ->
            ImageListItem(
                image = image,
                goToImageDetailsScreen = goToImageDetailsScreen,
                modifier = Modifier.padding(1.dp)
            )
        }
    }
}

@Composable
private fun ImageListItem(
    image: ImageModel,
    goToImageDetailsScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageComposable(
                image = image,
                isThumbnail = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        goToImageDetailsScreen.invoke(image.id)
                    },
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(id = R.string.by_username, image.username),
                textAlign = TextAlign.Justify
            )
        }
    }
}