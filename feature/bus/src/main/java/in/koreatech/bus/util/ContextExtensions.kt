package `in`.koreatech.bus.util

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity

internal fun Context.goToArticle(id: Int) {
    val intent =
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("koin://article/activity?fragment=article_detail&article_id=$id&board_id=4")
        }
    startActivity(intent)
}

internal fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
