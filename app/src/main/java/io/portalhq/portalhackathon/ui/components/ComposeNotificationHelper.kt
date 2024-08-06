package io.portalhq.portalhackathon.ui.components

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import io.portalhq.portalhackathon.core.notification.NotificationCommand

@Composable
fun ScreenNotification(
    snackbarHostState: SnackbarHostState,
    notificationCommand: NotificationCommand?
) {
    notificationCommand ?: return

    val context = LocalContext.current
    LaunchedEffect(key1 = notificationCommand) {
        when (notificationCommand) {
            is NotificationCommand.Important, is NotificationCommand.Error -> {
                snackbarHostState.showSnackbar(notificationCommand.resolveMessage(context))
            }
            is NotificationCommand.Normal -> {
                Toast.makeText(context, notificationCommand.resolveMessage(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
