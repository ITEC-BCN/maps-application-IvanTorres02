package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mapsapp.ui.screens.MapsScreen
import com.example.mapsapp.ui.screens.markerListScreen

@Composable
fun MainNavigationWrapper(navToNext: NavHostController, modifier: Modifier){
    NavHost(navToNext, Destination.Map){
        composable<Destination.Map>{
            MapsScreen()
        }
        composable<Destination.List>{
            markerListScreen()
        }
    }
}