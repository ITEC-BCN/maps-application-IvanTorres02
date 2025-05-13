package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.MySupabaseClient

class MyApp : Application() {

    companion object {
        lateinit var database: MySupabaseClient
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // No hace falta pasarle nada al constructor
        database = MySupabaseClient()
    }
}
