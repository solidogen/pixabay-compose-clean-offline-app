package com.example.pixabay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pixabay.ui.navigation.Navigation
import com.example.pixabay.ui.theme.PixabayComposeCleanOfflineAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixabayComposeCleanOfflineAppTheme {
                Navigation()
            }
        }
    }
}