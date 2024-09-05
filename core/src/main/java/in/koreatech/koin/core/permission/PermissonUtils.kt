package `in`.koreatech.koin.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

private val NOTIFICATION_REQUIRED_PERMISSION = mutableListOf<String>().apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}.toTypedArray()

fun Context.checkNotificationPermission() = NOTIFICATION_REQUIRED_PERMISSION.all {
    ContextCompat.checkSelfPermission(
        this, it
    ) == PackageManager.PERMISSION_GRANTED
}