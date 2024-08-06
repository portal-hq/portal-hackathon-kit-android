package io.portalhq.portalhackathon.ui.components

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.portalhq.portalhackathon.core.notification.NotificationCommand
import io.portalhq.portalhackathon.core.viewstate.ViewState

@Composable
fun ScreenNotification(
    snackbarHostState: SnackbarHostState,
    notificationCommand: NotificationCommand
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = notificationCommand) {
        snackbarHostState.showSnackbar(notificationCommand.resolveMessage(context))
    }
}

@Composable
fun <V: ViewState> ScreenNotification(notificationCommand: NotificationCommand) {
    val context = LocalContext.current
    LaunchedEffect(key1 = notificationCommand) {
        Toast.makeText(context, notificationCommand.resolveMessage(context), Toast.LENGTH_SHORT).show()
    }
}
