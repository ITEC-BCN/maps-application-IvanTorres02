package com.example.mapsapp.utils

import com.example.mapsapp.BuildConfig
import com.example.mapsapp.SupabaseApplication
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession

class SupabaseManager {
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val supabaseKey = BuildConfig.SUPABASE_KEY
}
suspend fun signUpWithEmail(emailValue: String, passwordValue: String): AuthState {
    return try {
        SupabaseApplication.database.client.auth.signUpWith(Email) {
            email = emailValue
            password = passwordValue
        }
        AuthState.Authenticated
    } catch (e: Exception) {
        AuthState.Error(e.localizedMessage ?: "Error desconocido")
    }
}

suspend fun signInWithEmail(emailValue: String, passwordValue: String): AuthState {
    try {
        SupabaseApplication.database.client.auth.signInWith(Email) {
            email = emailValue
            password = passwordValue
        }
        return AuthState.Authenticated
    } catch (e: Exception) {
        return AuthState.Error(e.localizedMessage)
    }
}
fun retrieveCurrentSession(): UserSession?{
    val session = SupabaseApplication.database.client.auth.currentSessionOrNull()
    return session
}
fun refreshSession(): AuthState {
    try {
        SupabaseApplication.database.client.auth.currentSessionOrNull()
        return AuthState.Authenticated
    } catch (e: Exception) {
        return AuthState.Error(e.localizedMessage)
    }
}
