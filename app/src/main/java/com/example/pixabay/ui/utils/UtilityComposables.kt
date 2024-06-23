package com.example.pixabay.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataError

@Composable
fun VerticalSpace(padding: Dp) {
    Spacer(modifier = Modifier.padding(vertical = padding))
}

@Composable
fun ErrorState(
    error: DataError,
    onRetry: () -> Unit
) {
    val errorText = when (error) {
        is DataError.ErrorMessage -> error.message
        DataError.UnknownError -> stringResource(id = R.string.unknown_error)
    }
    Card {
        Column {
            Text(text = errorText)
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp? = null
) {
    CircularProgressIndicator(
        modifier = modifier.apply {
            size?.let { size(it) }
        }
    )
}

@Composable
fun ImageComposable(
    image: ImageModel,
    isThumbnail: Boolean,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    LoadableAsyncImage(
        model = if (isThumbnail) {
            image.thumbnailUrl
        } else {
            image.largeImageUrl
        },
        contentDescription = image.tagsString,
        isThumbnail = isThumbnail,
        modifier = modifier,
        cacheKey = image.id,
        contentScale = contentScale
    )
}

@Composable
fun LoadableAsyncImage(
    model: Any?,
    contentDescription: String?,
    isThumbnail: Boolean,
    modifier: Modifier = Modifier,
    cacheKey: String? = null,
    loadingIndicatorSize: Dp = 40.dp,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .placeholderMemoryCacheKey(cacheKey)
                .apply {
                    if (isThumbnail) {
                        diskCacheKey(cacheKey)
                            .memoryCacheKey(cacheKey)
                    }
                }
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .requiredSize(loadingIndicatorSize)
                        .padding(40.dp)
                )
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}
