package com.example.mapsapp.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

class MySupabaseClient() {
    lateinit var client: SupabaseClient
    constructor(supabaseUrl: String, supabaseKey: String): this(){
        client = createSupabaseClient(
            supabaseUrl = "https://qrpnckxmqnhzjjsdzqiw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFycG5ja3htcW5oempqc2R6cWl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5OTc2MDMsImV4cCI6MjA2MTU3MzYwM30.s2hezxvMoI2ERR1PTozgZE7Mq1bp-SeH1uHevNW8hL4"
        ) {
            install(Postgrest)
        }
    }
    suspend fun getAllMarkers(): List<Marker> {
        return client.from("Marker").select().decodeList<Marker>()
    }

    suspend fun getMarker(id: String): Marker{
        return client.from("Marker").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marker>()
    }
    suspend fun insertStudent(marker: Marker){
        client.from("Marker").insert(marker)
    }
    suspend fun updateMarker(id: String, name: String, mark: Double){
        client.from("Marker").update({
            set("name", name)
            set("mark", mark)
        }) { filter { eq("id", id) } }
    }
    suspend fun deleteMarker(id: String){
        client.from("Student").delete{ filter { eq("id", id) } }
    }

}
