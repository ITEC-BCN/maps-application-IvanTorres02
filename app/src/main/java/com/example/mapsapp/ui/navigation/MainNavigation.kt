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
    NavHost(navToNext, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen(modifier, navController = navToNext)
        }

        composable<Destination.CreateMarker> { backStackEntry ->
            val createMarker = backStackEntry.toRoute<Destination.CreateMarker>()
            // Obtener el ViewModel asociado al contexto de la actividad o pantalla
            val markerViewModel: MarkerViewModel = viewModel()

            CreateMarkerScreen(
                latitud = createMarker.latitud,
                longitud = createMarker.longitud,
                navController = navToNext,
                markerViewModel = markerViewModel // Pasamos el ViewModel a la pantalla
            )
        }


        composable<Destination.List> {
            MarkerListScreen()
        }
    }
}
