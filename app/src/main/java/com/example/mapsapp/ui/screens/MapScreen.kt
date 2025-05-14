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
    navController: NavController,
    viewModel: MarkerViewModel
) {
    Column(modifier= Modifier.fillMaxSize()){
        val initialPosition = LatLng(41.4534225, 2.1837151)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialPosition, 17f)
        }

        LaunchedEffect(Unit) {
            viewModel.getAllMarkers()
        }

        val markersList by viewModel.markersList.observeAsState(initial = emptyList())

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { Log.d("MAP CLICK", "Clicked on: $it") },
            onMapLongClick = { latLng ->
                navController.navigate(
                    Destination.CreateMarker(latitud = latLng.latitude, longitud = latLng.longitude)
                )
            }
        ) {
            markersList.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitud, marker.longitud)),
                    title = marker.nombre,
                    snippet = marker.descripcion
                )
            }

            Marker(
                state = MarkerState(position = initialPosition),
                title = "ITB",
                snippet = "Institut Tecnològic de Barcelona"
            )
        }
    }

}
