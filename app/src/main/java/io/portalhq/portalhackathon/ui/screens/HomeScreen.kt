package io.portalhq.portalhackathon.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import io.portalhq.portalhackathon.presentation.home.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    Text(text = "Home Screen - Hello World with address: ${viewState.value.solanaAddress}")
}