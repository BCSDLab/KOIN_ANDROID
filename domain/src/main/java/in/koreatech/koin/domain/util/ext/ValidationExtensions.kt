package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.constant.GOOGLE_FORM_URL
import `in`.koreatech.koin.domain.constant.INSTAGRAM_URL
import `in`.koreatech.koin.domain.constant.OPEN_CHAT_URL
import `in`.koreatech.koin.domain.util.regex.EmailUtil
import `in`.koreatech.koin.domain.util.regex.PasswordUtil

fun String.isValidEmail(): Boolean = EmailUtil().isEmailValidate(this)

fun String.isBusinessValidEmail(): Boolean = EmailUtil().isBusinessEmailValidate(this)

fun String.isValidPassword() = PasswordUtil().isPasswordValidate(this)

fun String.isNotValidEmail() = !isValidEmail()

fun String.isNotBusinessValidEmail() = !isBusinessValidEmail()

fun String.isNotValidPassword() = !isValidPassword()

fun String.isInstagramUrl(): Boolean = this.startsWith(INSTAGRAM_URL)

fun String.isGoogleFormUrl(): Boolean = this.startsWith(GOOGLE_FORM_URL)

fun String.isOpenChatUrl(): Boolean = this.startsWith(OPEN_CHAT_URL)
