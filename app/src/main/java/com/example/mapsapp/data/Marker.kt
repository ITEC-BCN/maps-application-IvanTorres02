package com.example.mapsapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Marker(
    val id: String = "0",
    val nombre: String,
    val descripcion: String,
    val latitud: String,
    val longitud: String,
    val imagen: String
)
