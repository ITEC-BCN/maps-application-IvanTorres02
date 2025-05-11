package com.example.mapsapp.ui.screens

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.core.app.ActivityCompat
import com.example.mapsapp.viewmodels.MarkerViewModel

@Composable
fun CreateMarkerScreen(latitud: String, longitud: String, navController: NavController, markerViewModel: MarkerViewModel) {
    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }  // Usamos String en lugar de TextFieldValue
    var descripcion by remember { mutableStateOf("") }  // Usamos String en lugar de TextFieldValue
    var imagenUri: MutableState<Uri?> = remember { mutableStateOf(null) }
    var bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    var isImagePicked by remember { mutableStateOf(false) }  // Track if an image is selected

    val context = LocalContext.current

    // Definir el launcher para tomar una foto
    val takePictureLauncher: ActivityResultLauncher<Uri> = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            imagenUri.value?.let { uri ->
                // Convertir URI en Bitmap
                val contentResolver = context.contentResolver
                val bitmapResult = contentResolver.openInputStream(uri)?.let {
                    BitmapFactory.decodeStream(it)
                }
                bitmap.value = bitmapResult
            }
        }
    }

    // Definir el launcher para seleccionar imagen de la galería
    val selectImageLauncher: ActivityResultLauncher<String> = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri.value = uri
        uri?.let {
            // Convertir URI en Bitmap
            val contentResolver = context.contentResolver
            val bitmapResult = contentResolver.openInputStream(it)?.let {
                BitmapFactory.decodeStream(it)
            }
            bitmap.value = bitmapResult
        }
    }

    // Verificar si la aplicación tiene permisos para leer imágenes
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)  // Solo por ejemplo, ya no necesitas esta constante
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Crear un nuevo marcador")
        Spacer(modifier = Modifier.height(50.dp))  // Esto ocupará el máximo espacio disponible entre los elementos

        // Campo para el nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Campo para la descripción
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mostrar la imagen si existe
        imagenUri.value?.let {
            Image(
                bitmap = bitmap.value?.asImageBitmap() ?: return@let,
                contentDescription = "Imagen del marcador",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botones para seleccionar la foto o tomar una foto (separados)
        if (!isImagePicked) {
            Button(onClick = {
                selectImageLauncher.launch("image/*") // Iniciar galería para seleccionar imagen
            }) {
                Text("Seleccionar foto desde la galería")
            }

            Button(onClick = {
                val photoUri: Uri = createImageUri(context) // Crea la URI para almacenar la foto
                takePictureLauncher.launch(photoUri) // Iniciar cámara para tomar una foto
            }) {
                Text("Tomar una foto")
            }
        }

        // Botón para guardar el marcador
        Button(onClick = {
            if (nombre.isNotBlank() && descripcion.isNotBlank() && imagenUri.value != null) {
                val id = "some_unique_id"  // Aquí puedes generar un id único o usar otro mecanismo para obtenerlo
                markerViewModel.insertNewMarker(id, nombre, descripcion, latitud, longitud, imagenUri.value.toString())
                navController.popBackStack()  // Regresar a la pantalla anterior
            } else {
                // Mostrar un mensaje de error si los campos están vacíos
                Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Guardar marcador")
        }
    }
}

// Para crear la URI que almacene la imagen tomada
fun createImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "image_title")
        put(MediaStore.Images.Media.DESCRIPTION, "image_description")
    }
    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}
