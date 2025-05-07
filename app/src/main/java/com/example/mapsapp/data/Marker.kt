package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id : String,
    val nombre : String,
    val latitud: Int,
    val longitud: Int,
    val imagen: String
)
