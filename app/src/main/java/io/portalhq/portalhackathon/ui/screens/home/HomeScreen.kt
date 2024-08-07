package io.portalhq.portalhackathon.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.portalhq.portalhackathon.presentation.home.HomeViewModel
import io.portalhq.portalhackathon.presentation.home.HomeViewState
import io.portalhq.portalhackathon.ui.components.AppScaffold
import io.portalhq.portalhackathon.ui.components.ScreenNavigation
import io.portalhq.portalhackathon.ui.components.ScreenNotification

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = viewState.value.isRefreshing,
        onRefresh = { viewModel.refresh() }
    )
    AppScaffold(
        title = "Portal Hackathon",
        snackbarHostState = snackbarHostState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullToRefreshState)
                .verticalScroll(rememberScrollState())
        ) {
            HomeScreenUI(viewModel = viewModel, viewState = viewState.value)
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = viewState.value.isRefreshing,
                state = pullToRefreshState
            )
        }
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
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (viewState.walletAddress == null) {
            Button(onClick = { viewModel.generateWallet() }) {
                Text(text = "Generate Wallet")
            }
            return
        }

        SelectionContainer {
            Text(text = "Wallet Address: ${viewState.walletAddress}")
        }
        Text(text = "Solana Balance: ${viewState.solanaBalance}")
        Text(text = "PyUSD Balance: ${ viewState.pyUsdBalance }")
    }
}