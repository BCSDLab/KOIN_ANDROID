package `in`.koreatech.koin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class DiningNotificationService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DiningNoti", "test")

        return super.onStartCommand(intent, flags, startId)
    }
}