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
        val balance = portalRepository.getWalletBalance()
        updateState { it.copy(
            solanaBalance = balance.solanaBalance,
            pyUsdBalance = balance.pyUsdBalance)
        }
    }

    fun generateWallet() {
        launchOperation {
            portalRepository.createWallet()
            val walletAddress = portalRepository.getWalletAddress()
            updateState { it.copy(walletAddress = walletAddress) }
        }
    }

    fun sendPyUsd(amount: String, recipientAddress: String) {
        if (recipientAddress.isBlank()) {
            notify("Please enter a valid recipient address")
            return
        }

        if (amount.isBlank() || amount.toIntOrNull() == null){
            notify("Please enter a valid amount")
            return
        }

        launchOperation {
            val transactionHash = portalRepository.sendPyUSD(amount, recipientAddress)
            updateState { it.copy(mostRecentTransactionHash = transactionHash) }
            notify("Transaction sent successfully")
            fetchWalletBalance()
        }
    }

    fun backupWallet() {
        launchOperation {
            portalRepository.backupWalletWithPassword()
            notify("Wallet backed up successfully")
        }
    }

    fun recoverWallet() {
        launchOperation {
            portalRepository.recoverWalletWithPassword()
            notify("Wallet recovered successfully")
        }
    }

    private fun launchOperation(operation: suspend () -> Unit) {
        launchWithDefaultErrorHandling(onEndWithError = {
            updateState { it.copy(isDataLoading = false, isRefreshing = false) }
        }) {
            updateState { it.copy(isDataLoading = !it.isRefreshing) }
            operation()
            updateState { it.copy(isDataLoading = false, isRefreshing = false) }
        }
    }
}