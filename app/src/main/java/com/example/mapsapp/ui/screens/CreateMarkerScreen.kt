package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri.value != null) {
            context.contentResolver.openInputStream(imageUri.value!!)?.use { stream ->
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            context.contentResolver.openInputStream(it)?.use { stream ->
                bitmap.value = BitmapFactory.decodeStream(stream)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Abrir Cámara") },
            text = { Text("¿Quieres abrir la cámara para tomar una foto?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val uri = createImageUri(context)
                    imageUri.value = uri
                    launcher.launch(uri!!)
                }) {
                    Text("Tomar Foto")
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
        Text("Título", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Escribe el título") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(34.dp))

        Text("Descripción", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Escribe la descripción") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

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

        Row {
            TextButton(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
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

        Button(
            onClick = {
                val imageString = bitmap.value?.let { encodeBitmapToBase64(it) } ?: ""
                val id = System.currentTimeMillis()
                viewModel.insertNewMarker(id, title, description, latitud, longitud, imageString)
                navigateBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            )
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = navigateBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Cancelar")
        }
    }
}

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

fun encodeBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = java.io.ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT)
}
