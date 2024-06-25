package com.example.pixabay.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.pixabay.R
import com.example.pixabay.domain.model.ImageModel
import com.example.pixabay.domain.utils.DataError

const val THUMBNAIL_CACHE_KEY_AFFIX = "---THUMBNAIL"
const val LARGE_IMAGE_CACHE_KEY_AFFIX = "---LARGE_IMAGE"

@Composable
fun ImageComposable(
    image: ImageModel,
    isThumbnail: Boolean,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    LoadableAsyncImage(
        model = image.getImageUrl(isThumbnail),
        contentDescription = image.tagsString,
        modifier = modifier,
        cacheKey = image.getCacheKey(isThumbnail),
        contentScale = contentScale,
    )
}

@Composable
fun LoadableAsyncImage(
    model: Any?,
    contentDescription: String?,
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
                .diskCacheKey(cacheKey)
                .memoryCacheKey(cacheKey)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .requiredSize(loadingIndicatorSize)
                            .padding(40.dp)
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    ErrorState(error = DataError.UnknownError)
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { onQueryChange(it) },
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            )
        },
        placeholder = { Text("Search...") },
        modifier = modifier.testTag("SearchBar")
    ) {
        Text(text = "Content???")
    }
}

@Composable
fun CustomChip(text: String) {
    Text(
        text = text,
        fontSize = 10.sp,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 2.dp)
    )
}

@Composable
fun BoldJustifiedText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Justify,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun VerticalSpace(padding: Dp) {
    Spacer(modifier = Modifier.padding(vertical = padding))
}

@Composable
fun ErrorState(
    error: DataError,
    onRetry: (() -> Unit)? = null
) {
    val errorText = when (error) {
        is DataError.ErrorMessage -> error.message
        DataError.UnknownError -> stringResource(id = R.string.unknown_error)
    }
    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorText)
            VerticalSpace(padding = 4.dp)
            onRetry?.let {
                Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
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
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Go back"
        )
    }
}