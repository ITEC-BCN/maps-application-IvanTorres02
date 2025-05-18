package com.example.mapsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mapsapp.viewmodels.MarkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMarkerScreen(
    markerId: Long,                     // ID del marcador que es vol mostrar
    viewModel: MarkerViewModel,        // ViewModel per accedir als marcadors
    onBack: () -> Unit,                // Callback per tornar enrere
    onEdit: () -> Unit                 // Callback per anar a l’edició del marcador
) {
    // Obtenim el marcador corresponent a l’ID proporcionat
    val marker by remember(markerId) {
        mutableStateOf(viewModel.markersList.value?.find { it.id == markerId })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalls del Marcador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar enrere")
                    }
                }
            )
        }
    ) { padding ->

        // Si el marcador existeix, mostrem els detalls
        marker?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Si té una imatge associada, la mostrem amb Coil
                if (it.imagen.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(it.imagen) // URL o path de la imatge
                                .crossfade(true) // Animació de transició
                                .build()
                        ),
                        contentDescription = "Imatge del marcador",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Mostrem la informació del marcador
                Text(text = "Nom: ${it.nombre}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Descripció: ${it.descripcion}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Latitud: ${it.latitud}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Longitud: ${it.longitud}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                // Botó per editar el marcador
                Button(onClick = onEdit) {
                    Text("Editar marcador")
                }
            }

        } ?: run {
            // Si no s’ha trobat el marcador (potser l’ID no existeix), mostrem un missatge
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Marcador no trobat", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
