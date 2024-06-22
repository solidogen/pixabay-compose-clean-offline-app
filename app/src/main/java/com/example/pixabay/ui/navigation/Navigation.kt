package com.example.pixabay.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.pixabay.ui.features.images.details.ImageDetailsScreen
import com.example.pixabay.ui.features.images.details.ImageDetailsViewModel
import com.example.pixabay.ui.features.images.list.ImageListScreen
import com.example.pixabay.ui.features.images.list.ImageListViewModel
import com.example.pixabay.ui.navigation.Destination.Images.Companion.ID_PLACEHOLDER_NO_BRACKETS

@Composable
fun Navigation() {
    Scaffold { paddingValues ->
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Destination.Images.ROOT_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            imagesNavGraph(navController = navController)
        }
    }
}

private fun NavGraphBuilder.imagesNavGraph(navController: NavController) {
    navigation(
        route = Destination.Images.ROOT_ROUTE,
        startDestination = Destination.Images.List.route
    ) {
        composable(route = Destination.Images.List.route) {
            val viewModel = hiltViewModel<ImageListViewModel>()
            ImageListScreen(
                viewModel = viewModel,
                goToImageDetailsScreen = { id ->
                    navController.navigate(Destination.Images.Details.createRoute(id))
                },
            )
        }

        composable(
            route = Destination.Images.Details.route,
            arguments = listOf(navArgument(ID_PLACEHOLDER_NO_BRACKETS) { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<ImageDetailsViewModel>()
            ImageDetailsScreen(viewModel)
        }
    }
}

sealed class Destination(
    val route: String
) {
    sealed class Images(route: String) : Destination(
        route = "$ROOT_ROUTE/$route"
    ) {
        data object List : Images("list")
        data object Details : Images("details/$ID_PLACEHOLDER") {
            fun createRoute(id: String) = Details.route.replace(ID_PLACEHOLDER, id)
        }

        companion object {
            const val ROOT_ROUTE = "images"
            const val ID_PLACEHOLDER = "{id}"
            const val ID_PLACEHOLDER_NO_BRACKETS = "id"
        }
    }
}