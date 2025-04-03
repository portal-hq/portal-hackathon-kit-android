package io.portalhq.portalhackathon.presentation.home

import io.portalhq.portalhackathon.core.viewstate.ViewState

data class HomeViewState(
    val isDataLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val walletAddress: String? = null,
    val solanaBalance: String? = null,
    val pyUsdBalance: String? = null,
    val mostRecentTransactionHash: String? = null,
) : ViewState {
    val areActionsAllowed = !isDataLoading && !isRefreshing
}