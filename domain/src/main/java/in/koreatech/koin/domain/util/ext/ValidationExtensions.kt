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


fun String.isOwnerEmailValid(): Boolean {
    val emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    return this.matches(emailRegex.toRegex())
}

fun String.isOwnerNotEmailValid() = !isOwnerEmailValid()