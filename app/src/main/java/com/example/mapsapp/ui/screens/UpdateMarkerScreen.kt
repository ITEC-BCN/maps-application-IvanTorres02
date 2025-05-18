package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mapsapp.viewmodels.MarkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMarkerScreen(
    id: Long = 0,
    nombre: String,
    descripcion: String,
    latitud: Double,
    longitud: Double,
    imagen: String,
    viewModel: MarkerViewModel,
    onBack: () -> Unit
) {
    // Variables d'estat locals per editar els valors del marcador
    var nom by remember { mutableStateOf(nombre) }
    var desc by remember { mutableStateOf(descripcion) }
    var lat by remember { mutableStateOf(latitud.toString()) }
    var long by remember { mutableStateOf(longitud.toString()) }
    var img by remember { mutableStateOf(imagen) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Marcador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar enrere")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Camps de text per editar el nom i descripció del marcador
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Descripció") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botó per desar els canvis i tornar enrere
            Button(
                onClick = {
                    viewModel.updateMarker(
                        id = id,
                        name = nom,
                        description = desc,
                        lat = lat.toDoubleOrNull() ?: latitud, // Control d'errors per si l'usuari entra valors invàlids
                        lon = long.toDoubleOrNull() ?: longitud,
                        imageUrl = img
                    )

                    onBack() // Tornar a la pantalla anterior un cop guardats els canvis
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Desar canvis")
            }
        }
    }
}
