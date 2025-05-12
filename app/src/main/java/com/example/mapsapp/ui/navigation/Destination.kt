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
    data class CreateMarker(val latitud: String, val longitud: String) : Destination()

    @Serializable
    object List : Destination()

}