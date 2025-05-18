package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.mapsapp.ui.navigation.Destination
import com.example.mapsapp.viewmodels.MarkerViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapsScreen(
    navController: NavController,        // Controlador de navegació per navegar entre pantalles
    viewModel: MarkerViewModel           // ViewModel que gestiona la llista de marcadors
) {
    Column(modifier= Modifier.fillMaxSize()) {

        // Posició inicial del mapa (per exemple, Barcelona)
        val initialPosition = LatLng(41.4534225, 2.1837151)

        // Estat de la càmera, incloent la posició i zoom inicial
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialPosition, 17f)
        }

        // Crida a la funció del ViewModel per carregar tots els marcadors
        LaunchedEffect(Unit) {
            viewModel.getAllMarkers()
        }

        // Obtenim l'estat observable de la llista de marcadors com un State
        val markersList by viewModel.markersList.observeAsState(initial = emptyList())

        // Component GoogleMap que mostra el mapa de Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),               // Ocupa tot l’espai disponible
            cameraPositionState = cameraPositionState,       // Posició de la càmera inicial

            // Opcional: Log de clic curt (no fa res visualment)
            onMapClick = { Log.d("MAP CLICK", "Clicked on: $it") },

            // Clic llarg: navegació a la pantalla per crear un nou marcador
            onMapLongClick = { latLng ->
                navController.navigate(
                    Destination.CreateMarker(latitud = latLng.latitude, longitud = latLng.longitude)
                )
            }
        ) {
            // Per cada marcador a la llista, el mostrem al mapa
            markersList.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitud, marker.longitud)), // Posició
                    title = marker.nombre,               // Títol visible quan es toca el marcador
                    snippet = marker.descripcion         // Subtítol amb més informació
                )
            }
        }
    }
}

