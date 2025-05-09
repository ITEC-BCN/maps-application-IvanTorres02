package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    object Permissions : Destination()

    @Serializable
    object Drawer : Destination()

    @Serializable
    object Map : Destination()

    @Serializable
    data class CreateMarker(val latitud: Double, val longitud: Double) : Destination()

    @Serializable
    object List : Destination()

}