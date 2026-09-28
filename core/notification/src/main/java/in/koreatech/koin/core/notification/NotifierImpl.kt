package `in`.koreatech.koin.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationResolvers: Map<String, @JvmSuppressWildcards NotificationResolver>
) : Notifier {
    companion object {
        private const val CHANNEL_ID = "koin_channel"
        private const val CHANNEL_NAME = "koin_default_channel"
        private const val CHANNEL_DESCRIPTION = "koin_notification_channel"
    }

    override fun sendNotification(
        data: Map<String, String>,
        intent: Intent
    ) {
        if (checkSelfPermission()) return

        val notificationManager = NotificationManagerCompat.from(context)
        val payload = NotificationPayload.from(data)
        val notification = notificationResolvers[payload.host]?.resolve(payload) ?: StandardNotification()
        val notificationId = notification.stableKey?.hashCode() ?: System.currentTimeMillis().toInt()

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
            context.createNotification {
                setSmallIcon(R.drawable.ic_notification_koin_logo)
                    .setContentTitle(payload.title)
                    .setContentText(payload.content)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)

                notification.applyTo(
                    context = context,
                    payload = payload,
                    builder = this,
                    notificationId = notificationId
                )
            }

        notificationManager.notify(notificationId, notificationBuilder)
    }

    private fun Context.createNotification(block: NotificationCompat.Builder.() -> Unit): Notification {
        ensureNotificationChannelExists()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .apply(block)
            .build()
    }

    private fun Context.ensureNotificationChannelExists() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun checkSelfPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    }
}
