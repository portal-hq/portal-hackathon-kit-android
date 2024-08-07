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
        launchWithDefaultErrorHandling(onEndWithError = {
            updateState { it.copy(isDataLoading = false) }
        }) {
            updateState { it.copy(isDataLoading = true) }
            val walletAddress = portalRepository.getWalletAddress()
            updateState { it.copy(isDataLoading = false, walletAddress = walletAddress) }
        }
    }

    fun generateWallet() {
        launchWithDefaultErrorHandling(onEndWithError = {
            updateState { it.copy(isDataLoading = false) }
        }) {
            updateState { it.copy(isDataLoading = true) }

            portalRepository.createWallet()
            val walletAddress = portalRepository.getWalletAddress()

            updateState { it.copy(isDataLoading = false, walletAddress = walletAddress) }
        }
    }
}