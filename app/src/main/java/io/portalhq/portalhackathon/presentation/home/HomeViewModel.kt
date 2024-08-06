package io.portalhq.portalhackathon.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.portalhq.portalhackathon.core.viewmodel.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewState>() {
    override fun defaultViewState() = HomeViewState()
}