package io.portalhq.portalhackathon.presentation.home

import io.portalhq.portalhackathon.core.viewstate.ViewState

data class HomeViewState(
    val isDataLoading: Boolean = false,
    val walletAddress: String? = null
) : ViewState