package com.example.pixabay.ui.features.images.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.BoldJustifiedText
import com.example.pixabay.ui.utils.CustomChip
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.ImageComposable
import com.example.pixabay.ui.utils.LoadingIndicator
import com.example.pixabay.ui.utils.VerticalSpace

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

        if (state.isLoading) {
            LoadingIndicator()
        }
        image?.let {
            ImageDetailsContainer(image = image)
        }
        state.error?.let {
            ErrorState(error = it)
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ImageDetailsContainer(image: ImageModel) {
    BoldJustifiedText(text = stringResource(id = R.string.by_username, image.username))
    VerticalSpace(padding = 2.dp)
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
    BoldJustifiedText(text = stringResource(id = R.string.downloads, image.downloads))
    BoldJustifiedText(text = stringResource(id = R.string.likes, image.likes))
    BoldJustifiedText(text = stringResource(id = R.string.comments, image.comments))
    ImageContainer(
        image = image,
        modifier = Modifier.fillMaxWidth().padding(1.dp)
    )
}

@Composable
private fun ImageContainer(
    image: ImageModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            ImageComposable(
                image = image,
                isThumbnail = false,
                modifier = Modifier,
                contentScale = ContentScale.Fit
            )
        }
    }
}