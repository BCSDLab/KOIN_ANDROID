package `in`.koreatech.koin.feature.store.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Int): String {
    val nf = NumberFormat.getNumberInstance(Locale.KOREA)
    return nf.format(price)
}