package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarkerViewModel : ViewModel() {

    private val database = MyApp.database

    private val _markerName = MutableLiveData<String>()
    val markerName = _markerName

    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription

    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    fun editNombre(value : String){
        _markerName.value=value
    }


    fun editDescripcion(value : String){
        _markerDescription.value=value
    }

    fun insertNewMarker(id: Long, name: String, description: String, lat: Double, lon: Double, image: String) {
        val newMarker = Marker(
            id = id,
            nombre = name,
            descripcion = description,
            latitud = lat,
            longitud = lon,
            imagen = image
        )
        CoroutineScope(Dispatchers.IO).launch {
            database.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val markers = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = markers
            }
        }
    }

    fun getMarker(id: Long) {
        if (_selectedMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerName.value = marker.nombre
                    _markerDescription.value = marker.descripcion
                }
            }
        }
    }

    fun updateMarker(id: Long, name: String, description: String, lat: Double, lon: Double, image: String) {
        val updatedMarker = Marker(id, name, description, lat, lon, image)
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(id, updatedMarker)
            getAllMarkers()
        }
    }

    fun deleteMarker(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteMarker(id)
            getAllMarkers()
        }
    }
}
