package io.portalhq.portalhackathon.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.portalhq.portalhackathon.core.commonconstants.BlockChainConstants
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
    var recipientAddress by remember {
        mutableStateOf(BlockChainConstants.SOLANA_TEST_ADDRESS)
    }

    var amount by remember {
        mutableStateOf("5")
    }

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
        Text(text = "Solana Balance: ${viewState.solanaBalance ?: "0"}")
        Text(text = "PyUSD Balance: ${ viewState.pyUsdBalance ?: "0" }")

        TextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            label = { Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = "Recipient Address"
            )},
            value = recipientAddress,
            singleLine = true,
            onValueChange = { value -> recipientAddress = value },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        TextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            label = { Text(text = "Enter Amount")},
            value = amount,
            singleLine = true,
            placeholder = { Text(text = "0.0") },
            onValueChange = { value -> amount = value },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.textFieldColors()
        )

        Button(onClick = {
            viewModel.sendPyUsd(
                amount = amount,
                recipientAddress = recipientAddress
            )
        }) {
            Text(text = "Transfer PYUSD")
        }

        if (viewState.mostRecentTransactionHash != null) {
            SelectionContainer {
                Text(text = "Most recent transaction hash: ${viewState.mostRecentTransactionHash}")
            }
        }
    }
}