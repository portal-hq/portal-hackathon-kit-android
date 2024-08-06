package io.portalhq.portalhackathon.presentation.home

import io.portalhq.portalhackathon.core.viewmodel.BaseViewModel

class HomeViewModel : BaseViewModel<HomeViewState>() {
    override fun defaultViewState() = HomeViewState()
}