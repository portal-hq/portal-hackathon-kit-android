package io.portalhq.portalhackathon.core.navigation

sealed class GeneralNavigationDestination : NavigationDestination {
    data object Back : GeneralNavigationDestination()
    data object Profile: GeneralNavigationDestination()
}
