package com.example.pixabay.ui.features.images.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataState
import com.example.pixabay.ui.utils.ErrorState
import com.example.pixabay.ui.utils.ImageComposable
import com.example.pixabay.ui.utils.LoadingIndicator

@Composable
fun ImageDetailsScreen(
    viewModel: ImageDetailsViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        val state by viewModel.state.collectAsState(DataState.loading())
        val image = state.data

        if (state.isLoading) {
            LoadingIndicator()
        }

        image?.let {
            Text(
                text = stringResource(id = R.string.by_username, image.username),
                textAlign = TextAlign.Justify
            )
            ImageContainer(
                image = image,
                modifier = Modifier.padding(1.dp)
            )
        }

        state.error?.let {
            ErrorState(error = it, onRetry = {})
        }
    }
}

@Composable
private fun ImageContainer(
    image: ImageModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            border = BorderStroke(1.dp, Color.Black),
            modifier = modifier
        ) {
            ImageComposable(
                image = image,
                url = { it.largeImageUrl },
                modifier = Modifier.wrapContentHeight(),
                contentScale = ContentScale.Fit
            )
        }
    }
}