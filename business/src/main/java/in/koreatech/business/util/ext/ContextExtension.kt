package `in`.koreatech.business.util.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun Context.navigateToPlayStore() {
    val appPackageName: String = packageName
    try {
        val appStoreIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
        appStoreIntent.setPackage("com.android.vending")
        ContextCompat.startActivity(this, appStoreIntent, null)
    } catch (exception: ActivityNotFoundException) {
        ContextCompat.startActivity(
            this,
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            ),
            null
        )
    }
}