package io.portalhq.portalhackathon.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import io.portalhq.portalhackathon.presentation.home.HomeViewModel
import io.portalhq.portalhackathon.presentation.home.HomeViewState
import io.portalhq.portalhackathon.ui.components.AppScaffold

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    AppScaffold(title = "Portal Hackathon") {
        HomeScreenUI(viewModel = viewModel, viewState = viewState.value)
    }
}

@Composable
private fun HomeScreenUI(viewModel: HomeViewModel, viewState: HomeViewState) {
    Column {
        Text(text = "Home Screen - Hello World with address: ${viewState.solanaAddress}")
    }
}