package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.MarkerListScreen

@Composable
fun MainNavigationWrapper(navToNext: NavHostController, modifier: Modifier) {
    NavHost(navToNext, Destination.Map) {
        composable<Destination.Map> {
            MapsScreen(modifier, navController = navToNext)
        }

        composable<Destination.CreateMarker> { backStackEntry ->
            val createMarker = backStackEntry.toRoute<Destination.CreateMarker>()
            CreateMarkerScreen(
                latitud = createMarker.latitud,
                longitud = createMarker.longitud
            )
        }

        composable<Destination.List> {
            MarkerListScreen()
        }
    }
}
