package com.example.pixabay.ui.features.images.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pixabay.ui.utils.VerticalSpace

@Composable
fun ImageListScreen(
    // todo hiltViewModel
    goToImageDetailsScreen: (String) -> Unit
) {
    // todo private composable method for ImagesList
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
//        val state by meditationsViewModel.state.collectAsState()
        Text(text = "Images list screen")
        VerticalSpace(padding = 16.dp)
//        if (state.meditations.isEmpty()) {
//            CircularProgressIndicator()
//        } else {
//            MeditationList(state.meditations)
//        }
//        VerticalSpace(padding = 16.dp)
//        Button(onClick = meditationsViewModel::resetState) {
//            Text(text = "Reload meditations")
//        }
        Button(onClick = { goToImageDetailsScreen.invoke("1234") }) {
            Text(text = "Go to image details screen, 1234")
        }
    }
}