package `in`.koreatech.koin.util.ext

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity

fun TextView.useUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

val ComponentActivity.statusBarHeight: Int
    get() {
        val rect = Rect()
        window!!.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        return statusBarHeight
    }
