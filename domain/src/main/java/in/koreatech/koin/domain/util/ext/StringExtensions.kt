package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.PasswordUtil
import java.util.*

fun String.toSHA256() = PasswordUtil.generateSHA256(this)

val String.isValidStudentId: Boolean get() {
    if (this.trim().length < 10) {
        return false
    }

    val year: Int = this.trim().substring(0..3).toInt()
    return year in 1992..Calendar.getInstance().get(Calendar.YEAR)
}