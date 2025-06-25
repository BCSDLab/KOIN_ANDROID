package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.constant.INSTAGRAM_URL
import `in`.koreatech.koin.domain.util.regex.PasswordUtil

fun String.toSHA256() = PasswordUtil().generateSHA256(this)

fun String.toUnderlineForHtml() = "<u>$this</u>"

fun String.toColorForHtml(color: String) = "<font color = '#${color.substring(3)}'>$this</font>" // color = #ff000000 형태

fun String.formatInstagramUrlForm() = "${INSTAGRAM_URL}/$this"

fun String.formatInstagramLinkForm() = "@${this.removePrefix("${INSTAGRAM_URL}/").removeSuffix("/")}"

fun Int.formatTime(): String {
    val time = this
    val minute = time / 60
    val second = time % 60
    return String.format("%02d:%02d", minute, second)
}

fun String.formatPhoneNumber(): String = this.replace(Regex("(\\d{3})-?(\\d{4})-?(\\d{4})"), "$1-$2-$3")

fun String.formatBusinessNumber(): String = this.replace(Regex("(\\d{3})(\\d{2})(\\d{5})"), "$1-$2-$3")
