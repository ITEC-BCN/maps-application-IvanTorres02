package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class SupabaseApplication : Application() {

    companion object {
        lateinit var database: MySupabaseClient
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = MySupabaseClient()
    }
}
