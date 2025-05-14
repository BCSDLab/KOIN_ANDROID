package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.regex.PasswordUtil
import java.util.Calendar

fun String.toSHA256() = PasswordUtil().generateSHA256(this)

val String.isValidStudentId: Boolean
    get() {
        // Korean student number is 10 digit
        // Foreign student number is 8 or 9 digit
        if (this.trim().length !in 8..10) {
            return false
        }

        // First 4 digits are year.
        // Check if the year is between 1992 and current year
        val year: Int = this.trim().substring(0..3).toInt()

        if (year !in 1992..Calendar.getInstance().get(Calendar.YEAR)) {
            return false
        }

        return this.matches(Regex("""^\d+${'$'}""")) // TODO: Create Regex file
    }

val String.isValidPhoneNumber: Boolean get() =
    this.trim().matches(Regex("""^(01[016789]{1})-?([0-9]{3,4})-?([0-9]{4})$"""))

fun String.toUnderlineForHtml() = "<u>$this</u>"

fun String.toColorForHtml(color: String) = "<font color = '#${color.substring(3)}'>$this</font>" // color = #ff000000 형태

fun String.isNameFormat(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$"""))

fun String.isLoginIdFormat(): Boolean = this.matches(Regex("""^[a-z0-9_.-]+${'$'}""")) && this.length in 5..13

fun String.isNicknameFormat(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ가-힣a-zA-Z0-9]+${'$'}"""))

fun String.formatPhoneNumber(): String = this.replace(Regex("(\\d{3})-?(\\d{4})-?(\\d{4})"), "$1-$2-$3")

fun String.formatBusinessNumber(): String = this.replace(Regex("(\\d{3})(\\d{2})(\\d{5})"), "$1-$2-$3")

fun String.containsKorean(): Boolean = Regex("""[ㄱ-ㅎㅏ-ㅣ가-힣]""").containsMatchIn(this)

fun String.isKorean(): Boolean = this.matches(Regex("""^[ㄱ-ㅎㅏ-ㅣ가-힣]+${'$'}"""))

fun String.isEnglish(): Boolean = this.matches(Regex("""^[A-Za-z]+${'$'}"""))

fun Int.formatTime(): String {
    val time = this
    val minute = time / 60
    val second = time % 60
    return String.format("%02d:%02d", minute, second)
}
