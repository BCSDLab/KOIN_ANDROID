package `in`.koreatech.koin.core.notification

import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat

data class StandardNotification(
    override val stableKey: String? = null
) : NotificationType {
    override fun applyTo(context: Context, payload: NotificationPayload, builder: NotificationCompat.Builder, notificationId: Int) {
        val image = payload.imageUrl
            ?.takeIf(String::isNotBlank)
            ?.let { NotificationImageProvider.getBitmap(context, it) }
        if (image == null) {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(payload.content))
        } else {
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
                    .bigLargeIcon(null as Bitmap?)
                    .setBigContentTitle(payload.title)
                    .setSummaryText(payload.content)
            )
        }
    }
}
