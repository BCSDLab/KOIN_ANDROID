package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.EmailUtil
import `in`.koreatech.koin.domain.util.PasswordUtil

fun String.isValidEmail(): Boolean {
    val frontEmail = this.split("@")[0]

    if(frontEmail.isBlank()) return false
    return EmailUtil.isEmailValidate(frontEmail)
}
fun String.isValidPassword() = PasswordUtil.isPasswordValidate(this)
fun String.isNotValidEmail() = !isValidEmail()
fun String.isNotValidPassword() = !isValidPassword()