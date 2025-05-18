package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.SupabaseApplication
import com.example.mapsapp.data.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MarkerViewModel : ViewModel() {

    val database = SupabaseApplication.database


    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList


    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewMarker(
        id: Long,
        name: String,
        description: String,
        lat: Double,
        lon: Double,
        image: Bitmap?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageUrl = image?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                database.uploadImage(byteArray)
            } ?: ""

            val newMarker = Marker(
                id = id,
                nombre = name,
                descripcion = description,
                latitud = lat,
                longitud = lon,
                imagen = imageUrl
            )

            database.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val markers = database.getAllMarcardor()
            withContext(Dispatchers.Main) {
                _markersList.value = markers
            }
        }
    }

    fun updateMarker(id: Long, name: String, description: String, lat: Double, lon: Double, imageUrl: String) {
        val updatedMarker = Marker(id, name, description, lat, lon, imageUrl)
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(id, updatedMarker)
            getAllMarkers()
        }
    }

    fun deleteMarker(marker: Marker) {
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteMarcardor(marker.id.toInt())
            if (marker.imagen.isNotEmpty()) {
                database.deleteImage(marker.imagen)
            }
            getAllMarkers()
        }
    }


}
