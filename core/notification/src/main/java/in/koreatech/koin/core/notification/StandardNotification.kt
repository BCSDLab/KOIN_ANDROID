package `in`.koreatech.koin.core.notification

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import java.net.HttpURLConnection
import java.net.URL
import timber.log.Timber

data class StandardNotification(
    override val stableKey: String? = null
) : NotificationType {
    override fun applyTo(context: Context, payload: NotificationPayload, builder: NotificationCompat.Builder, notificationId: Int) {
        val image = payload.imageUrl?.takeIf(String::isNotBlank)?.let(::loadImage)
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

    private fun loadImage(imageUrl: String): Bitmap? {
        return try {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.inputStream.use(BitmapFactory::decodeStream)
        } catch (e: Exception) {
            Timber.e(e, "Notification image download failed")
            null
        }
    }
}
