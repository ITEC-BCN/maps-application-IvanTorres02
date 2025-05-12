package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: Long = 0,
    val nombre: String,
    val descripcion: String,
    val latitud: Double,
    val longitud: Double,
    val imagen: String
)
