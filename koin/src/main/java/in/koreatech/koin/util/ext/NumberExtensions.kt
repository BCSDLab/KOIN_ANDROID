package `in`.koreatech.koin.util.ext

import java.text.NumberFormat
import java.util.Locale

fun Number.toStringWithComma(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US)
    return formatter.format(this)
}