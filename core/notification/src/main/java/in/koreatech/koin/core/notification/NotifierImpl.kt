package `in`.koreatech.koin.core.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.net.URL
import javax.inject.Inject

class NotifierImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : Notifier {
    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
        private const val KEY_IMAGE_URL = "imageUrl"
        private const val CHANNEL_ID = "koin_channel"
        private const val CHANNEL_NAME = "koin_default_channel"
        private const val CHANNEL_DESCRIPTION = "koin_notification_channel"
    }

    override fun sendNotification(data: Map<String, String>, intent: Intent) {
        if (checkSelfPermission()) return

        val notificationManager = NotificationManagerCompat.from(context)
        val notificationId: Int = (System.currentTimeMillis()).toInt()

        val title = data[KEY_TITLE]
        val content = data[KEY_CONTENT]
        val imageUrl = data[KEY_IMAGE_URL]

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val imageUrlToBitmap = try {
            BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())
        } catch (e: Exception) {
            Timber.e("Notification Image Url to Bitmap Error : ${e.message}")
            null
        }

        val notificationLayout = createRemoteViewLayout(R.layout.layout_small_content, title, content, imageUrlToBitmap)
        val notificationExpandedLayout = createRemoteViewLayout(R.layout.layout_big_content, title, content, imageUrlToBitmap)

        val notificationBuilder = context.createNotification {
            setSmallIcon(R.drawable.ic_notification_koin_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setGroup(null)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationExpandedLayout)
                .setCustomHeadsUpContentView(notificationLayout)
        }

        notificationManager.notify(notificationId, notificationBuilder)
    }

    private fun Context.createNotification(
        block: NotificationCompat.Builder.() -> Unit,
    ): Notification {
        ensureNotificationChannelExists()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .apply(block)
            .build()
    }

    private fun Context.ensureNotificationChannelExists() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun createRemoteViewLayout(
        layoutId: Int,
        title: String?,
        content: String?,
        imageUrlToBitmap: Bitmap?
    ): RemoteViews {
        val view = RemoteViews(context.packageName, layoutId)
        view.apply {
            setTextViewText(R.id.tv_title, title)
            setTextViewText(R.id.tv_content, content)
            setImageViewBitmap(R.id.iv_logo, imageUrlToBitmap)
        }
        return view
    }

    private fun checkSelfPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    }
}