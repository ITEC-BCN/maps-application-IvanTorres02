package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper(navToNext: NavHostController, modifier: Modifier = Modifier) {
    val markerViewModel: MarkerViewModel = viewModel()

    NavHost(
        navController = navToNext,
        startDestination = Destination.Map,
        modifier = modifier
    ) {
        composable<Destination.Map> {
            MapsScreen(
                navController = navToNext,
                viewModel = markerViewModel
            )
        }

        composable<Destination.CreateMarker> {
            val createMarker = it.toRoute<Destination.CreateMarker>()
            CreateMarkerScreen(
                latitud = createMarker.latitud,
                longitud = createMarker.longitud,
                navigateBack = { navToNext.popBackStack() },
                viewModel = markerViewModel
            )
        }

        composable<Destination.List> {
            MarkerListScreen(viewModel = markerViewModel)
        }
    }
}
