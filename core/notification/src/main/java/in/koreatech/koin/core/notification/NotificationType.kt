package `in`.koreatech.koin.core.notification

import android.content.Context
import androidx.core.app.NotificationCompat

sealed interface NotificationType {
    val stableKey: String?

    fun applyTo(
        context: Context,
        payload: NotificationPayload,
        builder: NotificationCompat.Builder,
        notificationId: Int
    )
}
