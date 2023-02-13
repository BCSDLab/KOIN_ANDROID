package `in`.koreatech.koin.util.ext

import android.util.DisplayMetrics
import android.util.TypedValue

fun Int.dpToIntPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(), DisplayMetrics()
).toInt()