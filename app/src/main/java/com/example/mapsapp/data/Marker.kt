package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val imagen: String
)
