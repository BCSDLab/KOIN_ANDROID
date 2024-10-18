package `in`.koreatech.koin.core.notification

import android.content.Intent

interface Notifier {
    fun sendNotification(data: Map<String, String>, intent: Intent)
}