package com.example.mapsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerListScreen(
    viewModel: MarkerViewModel,
    onMarkerClick: (Long) -> Unit = {} // Per navegar a DetailScreen
) {
    // Carrega inicial de marcadors
    LaunchedEffect(Unit) {
        viewModel.getAllMarkers()
    }

    val markersList by viewModel.markersList.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Llista de Marcadors") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            items(markersList) { marker ->
                MarkerItem(marker = marker, onClick = { onMarkerClick(marker.id) })
            }
        }
    }
}

@Composable
fun MarkerItem(marker: Marker, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (marker.imagen.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(marker.imagen)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Imatge del marcador",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column {
                Text(text = marker.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = marker.descripcion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
