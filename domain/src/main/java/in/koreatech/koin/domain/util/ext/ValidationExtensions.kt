package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.regex.EmailUtil
import `in`.koreatech.koin.domain.util.regex.PasswordUtil

fun String.isValidEmail(): Boolean = EmailUtil().isEmailValidate(this)

fun String.isBusinessValidEmail(): Boolean = EmailUtil().isBusinessEmailValidate(this)

fun String.isValidGeneralEmail(): Boolean = this.isBusinessValidEmail() // Business email and general email use same regex

fun String.isNotValidGeneralEmail() = !isValidGeneralEmail()

fun String.isValidPassword() = PasswordUtil().isPasswordValidate(this)

fun String.isNotValidEmail() = !isValidEmail()

fun String.isNotBusinessValidEmail() = !isBusinessValidEmail()

fun String.isNotValidPassword() = !isValidPassword()

fun String.isValidUrlScheme(): Boolean = this.matches(Regex("^https?://.*"))

fun String.isValidLoginId(): Boolean = this.matches(Regex("""^[a-z0-9_.-]+${'$'}""")) && this.length in 5..13

fun String.isValidInstagramUrl(): Boolean = this.matches(Regex("^https?://(www\\.|l\\.)?instagram\\.com/[a-zA-Z0-9]+.*"))

fun String.isValidGoogleFormUrl(): Boolean = this.matches(Regex("^https?://(docs\\.google\\.com/forms|forms\\.gle)/[a-zA-Z0-9]+.*"))

fun String.isValidOpenChatUrl(): Boolean = this.matches(Regex("^https?://open\\.kakao\\.com/[a-zA-Z0-9]+.*"))

val String.isValidStudentId: Boolean
    get() {
        // Korean student number is 10 digit
        // Foreign student number is 8 or 9 digit
        if (this.trim().length !in 8..10) {
            return false
        }

        return this.matches(Regex("""^\d+${'$'}""")) // TODO: Create Regex file
    }

val String.isValidPhoneNumber: Boolean get() =
    this.trim().matches(Regex("""^(01[016789]{1})-?([0-9]{3,4})-?([0-9]{4})$"""))

fun String.isValidName(): Boolean = this.matches(Regex("""^(?:[가-힣]{2,5}|[A-Za-z]{2,30})${'$'}"""))

fun String.isValidNickname(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ가-힣a-zA-Z0-9]+${'$'}"""))

fun String.containsKorean(): Boolean = Regex("""[ㄱ-ㅎㅏ-ㅣ가-힣]""").containsMatchIn(this)
