package `in`.koreatech.koin.util.ext

import `in`.koreatech.koin.R
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

fun Window.statusBarColor(
    @ColorInt color: Int,
    lightStatusBar: Boolean = false
) {
    if(color != statusBarColor) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            insetsController?.setSystemBarsAppearance(
                if (lightStatusBar) APPEARANCE_LIGHT_STATUS_BARS else 0,
                APPEARANCE_LIGHT_STATUS_BARS
            )

            this.statusBarColor = color

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int = decorView.systemUiVisibility
            flags = if(lightStatusBar) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            decorView.systemUiVisibility = flags

            statusBarColor = color
        } else {
            statusBarColor = color
        }
    }

}

fun Window.blueStatusBar() {
    statusBarColor(ContextCompat.getColor(context, R.color.colorPrimary))
}

fun Window.whiteStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        statusBarColor(ContextCompat.getColor(context, R.color.background), true)
    } else {
        statusBarColor(ContextCompat.getColor(context, R.color.gray8))
    }
}