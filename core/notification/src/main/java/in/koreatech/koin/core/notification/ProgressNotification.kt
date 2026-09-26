package `in`.koreatech.koin.core.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.toColorInt

data class ProgressNotification(
    override val stableKey: String,
    val current: Int,
    val maximum: Int
) : NotificationType {
    override fun applyTo(context: Context, payload: NotificationPayload, builder: NotificationCompat.Builder, notificationId: Int) {
        builder
            .setAutoCancel(false)
            .setContentIntent(null)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setRequestPromotedOngoing(true)
            .setStyle(
                NotificationCompat.ProgressStyle()
                    .setProgress(current)
                    .addProgressSegment(
                        NotificationCompat.ProgressStyle.Segment(maximum).setColor("#B611F5".toColorInt()) // primary500
                    )
                    .addProgressPoint(
                        NotificationCompat.ProgressStyle.Point(33).setColor("#7D08A4".toColorInt()) // primary700
                    )
                    .addProgressPoint(
                        NotificationCompat.ProgressStyle.Point(67).setColor("#7D08A4".toColorInt()) // primary700
                    )
                    .setProgressTrackerIcon(IconCompat.createWithResource(context, R.drawable.ic_notification_koin_logo))
                    .setStyledByProgress(true)
            )
    }
}
