package io.portalhq.portalhackathon.core.navigation

sealed class GeneralNavigationDestination : NavigationDestination {
    object Back : GeneralNavigationDestination()
    object Profile: GeneralNavigationDestination()
}
