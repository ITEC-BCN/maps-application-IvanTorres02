package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.screens.PermissionsScreen

@Composable
fun ModalNavigation(){
    val navController = rememberNavController()
    NavHost(navController, Destination.Permissions){
        composable<Destination.Permissions>{
            PermissionsScreen() {
                navController.navigate(Drawer)
            }
        }
        composable<Destination.Drawer>{
            MyDrawerMenu()
        }
    }
}