package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.regex.EmailUtil
import `in`.koreatech.koin.domain.util.regex.PasswordUtil

fun String.isValidEmail(): Boolean = EmailUtil().isEmailValidate(this)
fun String.isBusinessValidEmail(): Boolean = EmailUtil().isBusinessEmailValidate(this)
fun String.isValidPassword() = PasswordUtil().isPasswordValidate(this)
fun String.isNotValidEmail() = !isValidEmail()
fun String.isNotBusinessValidEmail() = !isBusinessValidEmail()
fun String.isNotValidPassword() = !isValidPassword()