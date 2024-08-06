package io.portalhq.portalhackathon.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import io.portalhq.portalhackathon.core.viewmodel.ScreenBaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ScreenBaseViewModel<HomeViewState>() {
    override fun defaultViewState() = HomeViewState()

    fun showToast(message: String) {
        notify(message)
    }

    fun showSnackbar(message: String) {
        notifyImportant(message)
    }
}