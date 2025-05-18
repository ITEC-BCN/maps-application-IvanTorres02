package com.example.mapsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mapsapp.viewmodels.MarkerViewModel
import com.example.mapsapp.data.Marker
import androidx.compose.ui.graphics.Color

@Composable
fun MarkerListScreen(
    viewModel: MarkerViewModel,
    onMarkerClick: (Long) -> Unit = {}
) {
    // Crida a carregar els marcadors quan la pantalla es mostra inicialment
    LaunchedEffect(Unit) {
        viewModel.getAllMarkers()
    }

    val markersList by viewModel.markersList.observeAsState(initial = emptyList())

    Scaffold(
    ) { padding ->

        // Llista vertical en que es mostren tots els marcadors de la base de dades
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Iterem amb índex per poder usar una clau única (id)
            itemsIndexed(markersList, key = { _, marker -> marker.id }) { _, marker ->

                // Estat per controlar el swipe (lliscament per eliminar)
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deleteMarker(marker)
                            true
                        } else {
                            false
                        }
                    }
                )

                // Composable per permetre l'eliminació amb lliscament
                SwipeToDismissBox(
                    state = dissmissState,

                    backgroundContent = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.padding(bottom = 15.dp)
                            )
                        }
                    }
                ) {
                    // Contingut principal de la pantalla, marcadors que permeten click i ens porta a la pantalla de detall
                    MarkerItem(marker) {
                        onMarkerClick(marker.id)
                    }
                }
            }
        }
    }
}
@Composable
fun MarkerItem(marker: Marker, onClick: () -> Unit) {
    // Tarja clicable per mostrar la informació del marcador
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Quan es toca, executem el callback
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Disseny en fila dels marcadors
        Row(modifier = Modifier.padding(16.dp)) {

            if (marker.imagen.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(marker.imagen)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Imagen del marcador",
                    modifier = Modifier
                        .size(64.dp) // Mida fixa
                        .padding(end = 16.dp), // Espai a la dreta
                    contentScale = ContentScale.Crop // Imatge tallada correctament
                )
            }

            // Columna amb el nom i descripció del marcador
            Column {
                Text(text = marker.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = marker.descripcion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
