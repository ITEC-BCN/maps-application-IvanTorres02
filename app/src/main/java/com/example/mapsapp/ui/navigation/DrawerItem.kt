package com.example.mapsapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn

import androidx.compose.ui.graphics.vector.ImageVector

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destination
) {
    Map(Icons.Default.LocationOn,"Mapa", Destination.Map),
    List(Icons.Default.List,"Marcadores", Destination.List)
}