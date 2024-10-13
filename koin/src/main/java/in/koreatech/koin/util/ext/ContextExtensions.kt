package `in`.koreatech.koin.util.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

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