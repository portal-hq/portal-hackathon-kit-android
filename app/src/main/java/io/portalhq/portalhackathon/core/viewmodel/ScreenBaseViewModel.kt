package io.portalhq.portalhackathon.core.viewmodel

import androidx.lifecycle.viewModelScope
import io.portalhq.portalhackathon.core.navigation.GeneralNavigationDestination
import io.portalhq.portalhackathon.core.navigation.NavigationDestination
import io.portalhq.portalhackathon.core.notification.NotificationCommand
import io.portalhq.portalhackathon.core.viewstate.ViewState
import io.portalhq.portalhackathon.utils.SingleEventStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class ScreenBaseViewModel<VIEW_STATE : ViewState> : BaseViewModel<VIEW_STATE>() {

    val navigationCommand = SingleEventStateFlow<NavigationDestination>()
    val notificationCommand = SingleEventStateFlow<NotificationCommand>()

    fun navigate(destination: NavigationDestination) {
        navigationCommand.emitCo(destination)
    }

    fun navigateBack() {
        navigationCommand.emitCo(GeneralNavigationDestination.Back)
    }

    fun notify(notification: NotificationCommand) {
        notificationCommand.emitCo(notification)
    }

    fun notify(message: String) {
        notificationCommand.emitCo(NotificationCommand.Normal(message))
    }

    fun notifyImportant(message: String) {
        notificationCommand.emitCo(NotificationCommand.Important(message))
    }

    fun notifyError(message: String?) {
        notificationCommand.emitCo(NotificationCommand.Error(message))
    }

    protected fun launchWithDefaultErrorHandling(
        onEndWithError: () -> Unit = {},
        block: suspend () -> Unit
    ) {
        launchWithErrorHandling(
            onError = {
                onEndWithError()
                notifyError(it.message ?: it.toString())
                Timber.e("onEndWithError $it")
            },
            block = block
        )
    }

    protected fun <T> Flow<T>.catchWithDefaultErrorHandler(doOnError: () -> Unit = {}): Flow<T> {
        return catch { cause: Throwable ->
            val message = cause.message ?: cause.toString()
            doOnError()
            notifyError(message)
            Timber.e(message)
        }
    }

    private fun <T> SingleEventStateFlow<T>.emitCo(value: T) {
        viewModelScope.launch {
            emit(value)
        }
    }
}
