package `in`.koreatech.bus.util

import android.content.Context
import android.content.Intent
import android.net.Uri

internal fun Context.goToArticle(id: Int) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("koin://article/activity?fragment=article_detail&article_id=$id&board_id=4")
    }
    startActivity(intent)
}