package com.example.pixabay.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
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
    Column {
        Text(text = errorText)
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun ImageComposable(
    image: ImageModel,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = image.thumbnailUrl,
        contentDescription = image.tags,
        modifier = modifier
    )
}