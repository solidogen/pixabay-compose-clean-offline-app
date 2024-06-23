package com.example.pixabay.ui.utils

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.imageLoader
import coil.memory.MemoryCache
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
    url: (ImageModel) -> String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    loadingIndicatorSize: Dp = 20.dp
) {
    LoadableAsyncImage(
        model = url(image),
        contentDescription = image.tagsString,
        modifier = modifier,
        contentScale = contentScale,
        placeholderMemoryCacheKey = image.id
    )

//    DeprecatedImageComposable(
//        image = image,
//        url = url,
//        modifier = modifier,
//        contentScale = contentScale
//    )
}

@Deprecated("use ImageComposable -> LoadableAsyncImage as a content")
@Composable
fun DeprecatedImageComposable(
    image: ImageModel,
    url: (ImageModel) -> String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    loadingIndicatorSize: Dp = 20.dp
) {
    SubcomposeAsyncImage(
        model = url(image),
        contentDescription = image.tagsString,
        modifier = modifier,
        contentScale = contentScale
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            CircularProgressIndicator(
                modifier = Modifier.size(loadingIndicatorSize)
            )
        } else {
            SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun LoadableAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeholderMemoryCacheKey: String? = null,
    loadingIndicatorSize: Dp = 40.dp,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader
    var placeholderBitmap by remember(placeholderMemoryCacheKey) { mutableStateOf<Bitmap?>(null) }
    var isLoading by rememberSaveable(placeholderMemoryCacheKey) { mutableStateOf(true) }

    LaunchedEffect(placeholderMemoryCacheKey) {
        placeholderMemoryCacheKey?.let {
            placeholderBitmap =
                imageLoader.memoryCache?.get(MemoryCache.Key(placeholderMemoryCacheKey))?.bitmap
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier,
            model = ImageRequest.Builder(context)
                .data(model)
                .apply {
                    placeholderMemoryCacheKey?.let {
                        placeholderMemoryCacheKey(MemoryCache.Key(it))
                    }
                }
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = { isLoading = false },
        )

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (placeholderBitmap == null) {
                LoadingIndicator(
                    size = loadingIndicatorSize,
                )
            }
            else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = (placeholderBitmap as Bitmap).asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                )
            }
        }
    }
}