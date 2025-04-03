package io.portalhq.portalhackathon.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.portalhq.portalhackathon.ui.screens.AppScreen
import io.portalhq.portalhackathon.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreen.Home.route) {
        composable(route = AppScreen.Home.route) {
            HomeScreen(navController)
        }
    }
}