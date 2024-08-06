package io.portalhq.portalhackathon.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.portalhq.portalhackathon.presentation.home.HomeViewModel
import io.portalhq.portalhackathon.presentation.home.HomeViewState
import io.portalhq.portalhackathon.ui.components.AppScaffold
import io.portalhq.portalhackathon.ui.components.ScreenNavigation
import io.portalhq.portalhackathon.ui.components.ScreenNotification

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    AppScaffold(
        title = "Portal Hackathon",
        snackbarHostState = snackbarHostState
    ) {
        HomeScreenUI(viewModel = viewModel, viewState = viewState.value)
    }

    val navigationCommand = viewModel.navigationCommand.collectAsState(initial = null)
    ScreenNavigation(
        navigationCommand = navigationCommand.value,
        navController = navController
    ) {
        // TODO: Handle navigation to next screen
    }

    val notificationCommand = viewModel.notificationCommand.collectAsState(initial = null)
    ScreenNotification(
        snackbarHostState = snackbarHostState,
        notificationCommand = notificationCommand.value
    )
}

@Composable
private fun HomeScreenUI(viewModel: HomeViewModel, viewState: HomeViewState) {
    var counter by remember {
        mutableIntStateOf(0)
    }
    Column {
        Text(text = "Home Screen - Hello World with address: ${viewState.solanaAddress}")
        Button(onClick = { viewModel.showToast("Hello World ${counter++}") }) {
            Text(text = "Show Toast")
        }
        Button(onClick = { viewModel.showSnackbar("Hello World ${counter++}") }) {
            Text(text = "Show Snackbar")
        }
    }
}