package io.portalhq.portalhackathon.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.portalhq.portalhackathon.core.viewmodel.ScreenBaseViewModel
import io.portalhq.portalhackathon.data.PortalRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val portalRepository: PortalRepository
) : ScreenBaseViewModel<HomeViewState>() {
    override fun defaultViewState() = HomeViewState()

    init {
        fetchWalletDetails()
    }

    fun refresh() {
        updateState { it.copy(isRefreshing = true) }
        fetchWalletDetails()
    }

    private fun fetchWalletDetails() {
        launchOperation {
            val walletAddress = portalRepository.getWalletAddress()
            updateState { it.copy(walletAddress = walletAddress) }
            if (walletAddress != null) {
                fetchWalletBalance()
            }
        }
    }

    private suspend fun fetchWalletBalance() {
        launchOperation {
            val balance = portalRepository.getWalletBalance()
            updateState { it.copy(
                solanaBalance = balance.solanaBalance,
                pyUsdBalance = balance.pyUsdBalance)
            }
        }
    }

    fun generateWallet() {
        launchOperation {
            portalRepository.createWallet()
            val walletAddress = portalRepository.getWalletAddress()
            updateState { it.copy(walletAddress = walletAddress) }
        }
    }

    private fun launchOperation(operation: suspend () -> Unit) {
        launchWithDefaultErrorHandling(onEndWithError = {
            updateState { it.copy(isDataLoading = false, isRefreshing = false) }
        }) {
            updateState { it.copy(isDataLoading = true) }
            operation()
            updateState { it.copy(isDataLoading = false, isRefreshing = false) }
        }
    }
}