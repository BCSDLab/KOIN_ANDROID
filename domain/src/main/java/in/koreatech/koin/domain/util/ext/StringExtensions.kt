package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.PasswordUtil

fun String.toSHA256() = PasswordUtil.generateSHA256(this)
fun String.toUnderlineForHtml() = "<u>$this</u>"
fun String.toColorForHtml(color: String) = "<font color = '#${color.substring(3)}'>$this</font>" //color = #ff000000 형태
