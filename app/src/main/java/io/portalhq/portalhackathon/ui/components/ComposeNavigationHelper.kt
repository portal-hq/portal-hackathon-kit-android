package io.portalhq.portalhackathon.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import io.portalhq.portalhackathon.core.navigation.GeneralNavigationDestination
import io.portalhq.portalhackathon.core.navigation.NavigationDestination

@Composable
fun ScreenNavigation(
    navigationCommand: NavigationDestination?,
    navController: NavController,
    onNavigateToDestination: (NavigationDestination) -> Unit
) {
    navigationCommand ?: return

    LaunchedEffect(key1 = navigationCommand) {
        when (navigationCommand) {
            is GeneralNavigationDestination.Back -> { navController.popBackStack() }
            else -> { onNavigateToDestination(navigationCommand) }
        }
    }
}
