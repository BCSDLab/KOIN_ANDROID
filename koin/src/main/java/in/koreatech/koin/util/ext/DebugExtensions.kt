package `in`.koreatech.koin.util.ext

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

val Context.isDebug: Boolean
    get() {
        var debuggable = false
        val pm: PackageManager = packageManager
        try {
            val appinfo = pm.getApplicationInfo(packageName, 0)
            debuggable = 0 != appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return debuggable
    }