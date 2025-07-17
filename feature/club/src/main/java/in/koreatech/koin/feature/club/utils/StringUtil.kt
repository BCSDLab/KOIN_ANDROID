package `in`.koreatech.koin.feature.club.utils

fun String.ellipsize(maxLength: Int = 10): String = if (length > maxLength) {
    "${substring(0..maxLength)}..."
} else {
    this
}
