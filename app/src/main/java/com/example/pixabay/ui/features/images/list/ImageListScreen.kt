package com.example.pixabay.ui.features.images.list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pixabay.R
import com.example.pixabay.data.utils.ComposeImmutableList
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.CustomChip
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.ImageComposable
import com.example.pixabay.ui.utils.TestTags
import com.example.pixabay.ui.utils.ImageSearchBar
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
        val state by viewModel.imagesState.collectAsState(DataState.loading())
        val query by viewModel.query.collectAsState()

        ImageSearchBar(
            query = query,
            onQueryChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .testTag(TestTags.SEARCH_BAR)
        )
        VerticalSpace(padding = 8.dp)

        state.data?.let { images ->
            ImageList(
                images = ComposeImmutableList.from(images),
                goToImageDetailsScreen = goToImageDetailsScreen
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            state.error?.let {
                ErrorState(error = it, onRetry = viewModel::retrySearch)
            }
            if (state.isLoading) {
                LoadingIndicator()
            }
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
    var imageForDetailsDialog by remember { mutableStateOf<ImageModel?>(null) }

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
                modifier = Modifier.padding(1.dp),
                showDialog = {
                    imageForDetailsDialog = it
                }
            )
        }
    }

    imageForDetailsDialog?.let { image ->
        AlertDialog(
            onDismissRequest = { imageForDetailsDialog = null },
            title = { Text(stringResource(id = R.string.liking_the_image)) },
            text = { Text(stringResource(id = R.string.press_ok_to_show_details)) },
            confirmButton = {
                Button(
                    onClick = {
                        goToImageDetailsScreen.invoke(image.id)
                        imageForDetailsDialog = null
                    }
                ) {
                    Text(stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = { imageForDetailsDialog = null }
                ) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun ImageListItem(
    image: ImageModel,
    modifier: Modifier = Modifier,
    showDialog: (ImageModel) -> Unit
) {
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
            .clickable { showDialog(image) }
            .testTag(TestTags.IMAGE_ITEM)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageComposable(
                image = image,
                isThumbnail = true,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = stringResource(id = R.string.by_username, image.username),
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold
            )
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
            ) {
                image.tagList.forEach { tag ->
                    CustomChip(text = tag)
                }
            }
            VerticalSpace(padding = 2.dp)
        }
    }
}

@Preview
@Composable
fun ImageListItemPreview() {
    ImageListItem(
        image = ImageModel(
            tagsString = "asd, g423g, 34g124gg, asdfadsfa, adfas, asdfg, asdf, asdf, asdf, asdf, asdf, 423g, 34g124gg, asdfadsfa, adfas, ",
            id = "id",
            largeImageUrl = "",
            thumbnailUrl = "",
            username = "username",
            likes = 1,
            comments = 1,
            downloads = 1
        ),
        showDialog = {}
    )
}