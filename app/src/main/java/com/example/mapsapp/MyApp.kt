package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp: Application() {
    companion object {
        lateinit var database: MySupabaseClient
    }
    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient(
            supabaseUrl = "https://qrpnckxmqnhzjjsdzqiw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFycG5ja3htcW5oempqc2R6cWl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5OTc2MDMsImV4cCI6MjA2MTU3MzYwM30.s2hezxvMoI2ERR1PTozgZE7Mq1bp-SeH1uHevNW8hL4"
        )
    }
}
