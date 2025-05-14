package com.example.mapsapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MySupabaseClient {
    lateinit var client: SupabaseClient
    lateinit var storage: Storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
    constructor(){
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
            install(Storage)

        }
        storage = client.storage

    }

    suspend fun getAllMarcardor(): List<Marker> {
        return client.from("Markers").select().decodeList<Marker>()
    }

    suspend fun getMarcardor(id: Int): Marker{
        return client.from("Markers").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker>()
    }

    suspend fun insertMarker(marker: Marker){
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
            filter {
                eq("id", id)
            }
        }
    }


    suspend fun deleteMarcardor(id: Int){
        client.from("Markers").delete{
            filter {
                eq("id", id)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imagePath = "image_${fechaHoraActual.format(formato)}.png"
        val result = storage.from("images").upload(path = imagePath, data = imageFile)
        return buildImageUrl(result.path)
    }

    fun buildImageUrl(imageFileName: String): String {
        return "$supabaseUrl/storage/v1/object/public/images/$imageFileName"
    }

    suspend fun deleteImage(imageUrl: String) {
        val imagePath = imageUrl.removePrefix("$supabaseUrl/storage/v1/object/public/images/")
        storage.from("images").delete(imagePath)
    }

}
