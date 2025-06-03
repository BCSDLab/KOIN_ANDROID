package `in`.koreatech.koin.feature.userinfo.util

import java.util.Locale

fun Int.secondToMinute(): String {
    val minute = this / 60
    val second = this % 60
    return String.format(Locale.KOREA, "%02d:%02d", minute, second)
}
