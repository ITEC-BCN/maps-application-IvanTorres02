package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.mapsapp.viewmodels.MarkerViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(
    latitud: Double,
    longitud: Double,
    navigateBack: () -> Unit,
    viewModel: MarkerViewModel
) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // Llençador per fer una foto amb la càmera
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri.value != null) {
            context.contentResolver.openInputStream(imageUri.value!!)?.use { stream ->
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }
    }

    // Llençador per seleccionar una imatge de la galeria
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            context.contentResolver.openInputStream(it)?.use { stream ->
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }
    }

    // Pregunta per confirmar si es vol obrir la camara
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Abrir Cámara") },
            text = { Text("¿Quieres abrir la cámara para hacer una foto?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    imageUri.value = uri
                    launcher.launch(uri!!) // S'inicia la càmera amb el URI temporal
                }) {
                    Text("Hacer Foto")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 85.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Camp per introduir el títol
        Text("Título", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Escribe el título") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(34.dp))

        // Camp per introduir la descripció del marker
        Text("Descripción", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Escribe la descripción") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Es mostra la imatge nomes si s'ha seleccionat una
        bitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botons per obrir càmera o seleccionar de galeria
        Row {
            TextButton(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text("Cámara")
            }

            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = { pickImageLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text("Galería")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botó per guardar el marker amb la informació actual
        Button(
            onClick = {
                val id = System.currentTimeMillis() // ID temporal basat en el temps actual
                viewModel.insertNewMarker(id, title, description, latitud, longitud, bitmap.value)
                navigateBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botó per cancel·lar i tornar enrere
        Button(
            onClick = navigateBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Cancelar")
        }
    }
}

// Funció auxiliar per crear un URI temporal per a la foto
fun createImageUri(context: android.content.Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
