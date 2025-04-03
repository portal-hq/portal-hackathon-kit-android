package io.portalhq.portalhackathon.ui.screens

sealed class AppScreen(val route: String) {
    data object Home : AppScreen("Home")
}