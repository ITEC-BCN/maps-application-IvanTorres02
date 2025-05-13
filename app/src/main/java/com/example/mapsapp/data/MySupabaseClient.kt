package com.example.mapsapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MySupabaseClient {

    lateinit var storage: Storage

    private val supabaseUrl = "https://qrpnckxmqnhzjjsdzqiw.supabase.co"
    private val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFycG5ja3htcW5oempqc2R6cWl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5OTc2MDMsImV4cCI6MjA2MTU3MzYwM30.s2hezxvMoI2ERR1PTozgZE7Mq1bp-SeH1uHevNW8hL4"

    var client: SupabaseClient

    constructor() {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(Storage)
        }
        storage = client.storage
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imagePath = "image_${fechaHoraActual.format(formato)}.png"
        val result = storage.from("images").upload(path = imagePath, data = imageFile)
        return buildImageUrl(result.path)
    }

    private fun buildImageUrl(imageFileName: String): String {
        return "$supabaseUrl/storage/v1/object/public/images/$imageFileName"
    }

    suspend fun getAllMarkers(): List<Marker> {
        return client.from("Markers").select().decodeList<Marker>()
    }

    suspend fun getMarker(id: Long): Marker {
        return client.from("Markers").select {
            filter { eq("id", id) }
        }.decodeSingle<Marker>()
    }

    suspend fun insertMarker(marker: Marker) {
        client.from("Markers").insert(marker)
    }

    suspend fun updateMarker(id: Long, updated: Marker) {
        client.from("Markers").update({
            set("nombre", updated.nombre)
            set("descripcion", updated.descripcion)
            set("latitud", updated.latitud)
            set("longitud", updated.longitud)
            set("imagen", updated.imagen)
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteMarker(id: Long) {
        client.from("Markers").delete {
            filter { eq("id", id) }
        }
    }
}
