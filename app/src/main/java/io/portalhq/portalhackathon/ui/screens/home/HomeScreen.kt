package io.portalhq.portalhackathon.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            if (viewState.value.isDataLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
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
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .fillMaxSize(),
    ) {
        if (viewState.walletAddress == null) {
            GenerateWallet(viewModel = viewModel, viewState = viewState)
        }

        if (viewState.walletAddress != null) {
            WalletDetails(viewState = viewState)
            TransferPyUSD(viewModel = viewModel, viewState = viewState)
        }
        WalletBackupAndRecovery(viewModel = viewModel, viewState = viewState)
    }
}

@Composable
private fun ColumnScope.GenerateWallet(viewModel: HomeViewModel, viewState: HomeViewState) {
    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "No wallet found on device. Let's create one!",
        style = MaterialTheme.typography.body2
    )

    Button(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 10.dp),
        onClick = { viewModel.generateWallet() },
        enabled = viewState.areActionsAllowed
    ) {
        Text(text = "Generate Wallet")
    }
}

@Composable
private fun ColumnScope.WalletDetails(viewState: HomeViewState) {
    Text(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = "Wallet Details",
        style = MaterialTheme.typography.h6.copy(fontSize = 22.sp)
    )

    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "Wallet Address",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    SelectionContainer {
        Text(
            text = viewState.walletAddress.orEmpty(),
            style = MaterialTheme.typography.body2
        )
    }

    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "Solana Balance",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    Text(
        text = "${viewState.solanaBalance ?: "0"} SOL",
        style = MaterialTheme.typography.body2
    )

    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "PYUSD Balance",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    Text(
        text = "${viewState.pyUsdBalance ?: "0"} PYUSD",
        style = MaterialTheme.typography.body2
    )
}

@Composable
private fun ColumnScope.TransferPyUSD(viewModel: HomeViewModel, viewState: HomeViewState) {
    var recipientAddress by remember {
        mutableStateOf(BlockChainConstants.SOLANA_TEST_ADDRESS)
    }

    var amount by remember {
        mutableStateOf("5")
    }

    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 10.dp),
        text = "Transfer PYUSD",
        style = MaterialTheme.typography.h6.copy(fontSize = 20.sp)
    )

    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "Recipient Address",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    TextField(
        modifier = Modifier
            .fillMaxSize(),
        value = recipientAddress,
        singleLine = true,
        onValueChange = { value -> recipientAddress = value },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
    )

    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = "Amount",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    TextField(
        modifier = Modifier
            .fillMaxSize(),
        value = amount,
        singleLine = true,
        placeholder = { Text(text = "0.0") },
        onValueChange = { value -> amount = value },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        colors = TextFieldDefaults.textFieldColors()
    )

    if (viewState.mostRecentTransactionHash != null) {
        SelectionContainer {
            Text(text = "Most recent transaction hash: ${viewState.mostRecentTransactionHash}")
        }
    }

    Button(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 10.dp),
        onClick = {
            viewModel.sendPyUsd(
                amount = amount,
                recipientAddress = recipientAddress
            )
        },
        enabled = viewState.areActionsAllowed
    ) {
        Text(text = "Transfer PYUSD")
    }
}

@Composable
private fun ColumnScope.WalletBackupAndRecovery(viewModel: HomeViewModel, viewState: HomeViewState) {
    var password by remember {
        mutableStateOf("0000")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp, bottom = 10.dp),
        text = "Backup & Recovery",
        style = MaterialTheme.typography.h6.copy(fontSize = 20.sp)
    )

    Text(
        modifier = Modifier.padding(bottom = 10.dp),
        text = "Password",
        style = MaterialTheme.typography.subtitle2.copy(fontSize = 16.sp)
    )
    TextField(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        value = password,
        singleLine = true,
        onValueChange = { value -> password = value },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            val description = if (isPasswordVisible) "Hide Password" else "Show Password"
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        }
    )

    if (viewState.walletAddress != null) {
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = { viewModel.backupWallet(password) },
            enabled = viewState.areActionsAllowed
        ) {
            Text(text = "Backup Wallet")
        }
    }

    Button(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        onClick = { viewModel.recoverWallet(password) },
        enabled = viewState.areActionsAllowed)
    {
        Text(text = "Recover Wallet")
    }
}