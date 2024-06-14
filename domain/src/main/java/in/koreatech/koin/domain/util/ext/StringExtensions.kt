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

fun String.formatPhoneNumber(): String =
    this.replace(Regex("(\\d{3})(\\d{4})(\\d{4})"), "$1-$2-$3")

fun String.formatBusinessNumber(): String =
    this.replace(Regex("(\\d{3})(\\d{2})(\\d{5})"), "$1-$2-$3")

fun String.toDepartmentString(): String = when(this) {
    "HRD학과" -> "HRD"
    "고용서비스정책학과" -> "고용서비스"
    "교양학부" -> "교양"
    "디자인ㆍ건축공학부" -> "디자인"
    "메카트로닉스공학부" -> "메카트로닉스"
    "산업경영학부" -> "산업경영"
    "에너지신소재화학공학부" -> "에너지신소재"
    "융합학과" -> "융합"
    "전기ㆍ전자ㆍ통신공학부" -> "전기"
    "컴퓨터공학부" -> "컴퓨터"
    "안전공학과" -> "안전"
    "기계공학부" -> "기계"
    else -> ""
}