package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.util.regex.EmailUtil
import `in`.koreatech.koin.domain.util.regex.PasswordUtil
import `in`.koreatech.koin.domain.util.regex.RegexPatterns

fun String.isValidEmail(): Boolean = EmailUtil().isEmailValidate(this)

fun String.isBusinessValidEmail(): Boolean = EmailUtil().isBusinessEmailValidate(this)

fun String.isValidGeneralEmail(): Boolean = this.isBusinessValidEmail() // Business email and general email use same regex

fun String.isNotValidGeneralEmail() = !isValidGeneralEmail()

fun String.isValidPassword() = PasswordUtil().isPasswordValidate(this)

fun String.isNotValidEmail() = !isValidEmail()

fun String.isNotBusinessValidEmail() = !isBusinessValidEmail()

fun String.isNotValidPassword() = !isValidPassword()

fun String.isValidUrlScheme(): Boolean = this.matches(RegexPatterns.url)

fun String.isValidLoginId(): Boolean = this.matches(RegexPatterns.loginId) && this.length in 5..13

fun String.isValidInstagramUrl(): Boolean = this.matches(RegexPatterns.instagramUrl)

fun String.isValidGoogleFormUrl(): Boolean = this.matches(RegexPatterns.googleFormUrl)

fun String.isValidOpenChatUrl(): Boolean = this.matches(RegexPatterns.openChatUrl)

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
    this.trim().matches(RegexPatterns.phoneExact)

fun String.isValidName(): Boolean = this.matches(RegexPatterns.name)

fun String.isValidNickname(): Boolean = this.matches(RegexPatterns.nickname)

fun String.containsKorean(): Boolean = RegexPatterns.korean.containsMatchIn(this)
