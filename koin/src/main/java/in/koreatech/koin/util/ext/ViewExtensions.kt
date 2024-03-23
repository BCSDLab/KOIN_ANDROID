package `in`.koreatech.koin.util.ext

import android.graphics.Paint
import android.view.View
import android.widget.TextView

fun TextView.useUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}
