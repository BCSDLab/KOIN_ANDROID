package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.constant.HTTPS_URL
import `in`.koreatech.koin.domain.constant.HTTP_URL
import `in`.koreatech.koin.domain.constant.INSTAGRAM_URL
import `in`.koreatech.koin.domain.util.regex.PasswordUtil

fun String.toSHA256() = PasswordUtil().generateSHA256(this)

fun String.toUnderlineForHtml() = "<u>$this</u>"

fun String.toColorForHtml(color: String) = "<font color = '#${color.substring(3)}'>$this</font>" // color = #ff000000 형태

fun String.toInstagramUrl() = "${HTTPS_URL}${INSTAGRAM_URL}/$this"

fun String.toInstagramLink() = "@${this.removeUrlScheme().removePrefix("www.").removePrefix("l.").removePrefix("${INSTAGRAM_URL}/").removeSuffix("/")}"

fun String.toHttpsUrl() = "${HTTPS_URL}${this.removeUrlScheme()}"

fun String.removeUrlScheme() = this.removePrefix(HTTPS_URL).removePrefix(HTTP_URL)

fun Int.formatTime(): String {
    val time = this
    val minute = time / 60
    val second = time % 60
    return String.format("%02d:%02d", minute, second)
}

fun String.formatPhoneNumber(): String = this.replace(Regex("""(010)-?([0-9]{4})-?([0-9]{4})"""), "$1-$2-$3")

fun String.formatBusinessNumber(): String = this.replace(Regex("""([0-9]{3})([0-9]{2})([0-9]{5})"""), "$1-$2-$3")
