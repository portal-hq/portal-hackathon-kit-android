package io.portalhq.portalhackathon.core.notification

import android.content.Context
import androidx.annotation.StringRes
import io.portalhq.portalhackathon.R

sealed class NotificationCommand(
    protected open val message: String?,
    @StringRes
    protected open val messageResId: Int? = null,
    @StringRes
    protected val defaultMessageResId: Int? = null
) {
    data class Normal(
        override val message: String?,
        @StringRes
        override val messageResId: Int? = null
    ) : NotificationCommand(message, messageResId)

    data class Important(
        override val message: String?,
        @StringRes
        override val messageResId: Int? = null
    ) : NotificationCommand(message, messageResId)

    data class Error(
        override val message: String? = null,
        @StringRes
        override val messageResId: Int? = null
    ) : NotificationCommand(message, messageResId, R.string.general_unknown_error_message)

    fun resolveMessage(context: Context): String {
        return when {
            messageResId != null -> context.getString(messageResId!!)
            message.isNullOrBlank().not() -> message!!
            defaultMessageResId != null -> context.getString(defaultMessageResId)
            else -> ""
        }
    }
}
