package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.EmailUtil
import `in`.koreatech.koin.domain.util.PasswordUtil

fun String.isValidEmail() = EmailUtil.isEmailValidate(this)
fun String.isValidPassword() = PasswordUtil.isPasswordValidate(this)
fun String.isNotValidEmail() = !isValidEmail()
fun String.isNotValidPassword() = !isValidPassword()