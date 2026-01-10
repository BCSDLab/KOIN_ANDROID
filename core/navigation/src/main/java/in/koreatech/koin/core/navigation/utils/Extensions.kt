package `in`.koreatech.koin.core.navigation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Context.buildIntent(vararg argument: Pair<String, Any?>) =
    Intent(this, T::class.java).apply {
        putExtras(bundleOf(*argument))
    }

fun Context.buildIntent(className: Class<*>, vararg argument: Pair<String, Any?>) =
    Intent(this, className).apply {
        putExtras(bundleOf(*argument))
    }

fun buildDeepLinkIntent(type: String?) = Intent(Intent.ACTION_VIEW).apply {
    data = "koin://$type/activity".toUri()
}

fun Context.isValidDeepLink(type: String?): Boolean {
    return buildDeepLinkIntent(type).resolveActivity(packageManager) != null
}

inline fun <reified T : Activity> Context.goToActivity(vararg argument: Pair<String, Any?>) {
    startActivity(buildIntent<T>(*argument))
}

/**
 * @input : koin://shop?id
 * @output : shop
 */
fun String.toHost(): String {
    val host = this.split("://", "?")
    return if (host.size > 1) host[1] else ""
}
