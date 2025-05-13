package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen
import com.example.mapsapp.viewmodels.MarkerViewModel

@Composable
fun MainNavigationWrapper(navToNext: NavHostController, modifier: Modifier) {
    val markerViewModel: MarkerViewModel = viewModel()

    NavHost(navToNext, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen(modifier, navController = navToNext)
        }

        composable<Destination.CreateMarker> { backStackEntry ->
            val createMarker = backStackEntry.toRoute<Destination.CreateMarker>()
            CreateMarkerScreen(
                latitud = createMarker.latitud,
                longitud = createMarker.longitud,
                viewModel = markerViewModel,
                navigateBack = { navToNext.popBackStack() }
            )
        }

        composable<Destination.List> {
            MarkerListScreen(viewModel = markerViewModel)
        }
    }
}
