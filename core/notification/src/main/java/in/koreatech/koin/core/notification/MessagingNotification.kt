package `in`.koreatech.koin.core.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.Person

data class MessagingNotification(
    override val stableKey: String,
    val sender: String,
    val message: String
) : NotificationType {
    override fun applyTo(context: Context, payload: NotificationPayload, builder: NotificationCompat.Builder, notificationId: Int) {
        builder.setStyle(createStyle(context, notificationId))
    }

    private fun createStyle(context: Context, notificationId: Int): NotificationCompat.MessagingStyle {
        val previousStyle =
            context.getSystemService(NotificationManager::class.java)
                .activeNotifications
                .firstOrNull { it.id == notificationId }
                ?.notification
                ?.let(NotificationCompat.MessagingStyle::extractMessagingStyleFromNotification)

        return (previousStyle ?: newStyle(context))
            .addMessage(createMessage(context))
    }

    private fun createMessage(context: Context): NotificationCompat.MessagingStyle.Message {
        val notificationMessage = NotificationCompat.MessagingStyle.Message(
            message,
            System.currentTimeMillis(),
            Person.Builder()
                .setName(sender)
                .build()
        )

        NotificationImageProvider.getUri(context, message)?.let { image ->
            notificationMessage.setData(image.mimeType, image.uri)
        }
        return notificationMessage
    }

    private fun newStyle(context: Context): NotificationCompat.MessagingStyle {
        return NotificationCompat.MessagingStyle(
            Person.Builder()
                .setName(context.getString(R.string.notification_user_name))
                .build()
        ).setGroupConversation(false)
    }
}
