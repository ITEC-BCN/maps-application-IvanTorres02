package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MarkerViewModel : ViewModel() {

    private val database = MyApp.database

    private val _markerName = MutableLiveData<String>()
    val markerName = _markerName

    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription

    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    fun editNombre(value: String) {
        _markerName.value = value
    }

    fun editDescripcion(value: String) {
        _markerDescription.value = value
    }

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

    fun getMarker(id: Long) {
        if (_selectedMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getMarcardor(id.toInt())
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerName.value = marker.nombre
                    _markerDescription.value = marker.descripcion
                }
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

    fun deleteMarker(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteMarcardor(id.toInt())
            getAllMarkers()
        }
    }

}
