package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.regex.PasswordUtil
import java.util.Calendar

fun String.toSHA256() = PasswordUtil().generateSHA256(this)

val String.isValidStudentId: Boolean get() {
    if (this.trim().length != 10) {
        return false
    }

    val year: Int = this.trim().substring(0..3).toInt()
    return year in 1992..Calendar.getInstance().get(Calendar.YEAR)
}

fun String.toUnderlineForHtml() = "<u>$this</u>"
fun String.toColorForHtml(color: String) = "<font color = '#${color.substring(3)}'>$this</font>" //color = #ff000000 형태

fun String.isNameFormat(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+&"""))

fun String.formatPhoneNumber(): String =
    this.replace(Regex("(\\d{3})(\\d{4})(\\d{4})"), "$1-$2-$3")

fun String.formatBusinessNumber(): String =
    this.replace(Regex("(\\d{3})(\\d{2})(\\d{5})"), "$1-$2-$3")

