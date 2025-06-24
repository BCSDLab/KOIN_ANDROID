package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.constant.GOOGLE_FORM_URL
import `in`.koreatech.koin.domain.constant.INSTAGRAM_URL
import `in`.koreatech.koin.domain.constant.OPEN_CHAT_URL
import `in`.koreatech.koin.domain.util.regex.EmailUtil
import `in`.koreatech.koin.domain.util.regex.PasswordUtil
import java.util.Calendar

fun String.isValidEmail(): Boolean = EmailUtil().isEmailValidate(this)

fun String.isBusinessValidEmail(): Boolean = EmailUtil().isBusinessEmailValidate(this)

fun String.isValidPassword() = PasswordUtil().isPasswordValidate(this)

fun String.isNotValidEmail() = !isValidEmail()

fun String.isNotBusinessValidEmail() = !isBusinessValidEmail()

fun String.isNotValidPassword() = !isValidPassword()

fun String.isInstagramUrl(): Boolean = this.startsWith(INSTAGRAM_URL)

fun String.isGoogleFormUrl(): Boolean = this.startsWith(GOOGLE_FORM_URL)

fun String.isOpenChatUrl(): Boolean = this.startsWith(OPEN_CHAT_URL)

val String.isValidStudentId: Boolean
    get() {
        if (this.trim().length != 10) {
            return false
        }

        val year: Int = this.trim().substring(0..3).toInt()
        return year in 1992..Calendar.getInstance().get(Calendar.YEAR)
    }

val String.isValidPhoneNumber: Boolean get() =
    this.trim().matches(Regex("""^(01[016789]{1})-?([0-9]{3,4})-?([0-9]{4})$"""))

fun String.isNameFormat(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$"""))

fun String.isNicknagmeFormat(): Boolean = this.matches(Regex("""^[ㄱ-ㅎ가-힣a-zA-Z0-9]+${'$'}"""))

fun String.formatPhoneNumber(): String = this.replace(Regex("(\\d{3})-?(\\d{4})-?(\\d{4})"), "$1-$2-$3")

fun String.formatBusinessNumber(): String = this.replace(Regex("(\\d{3})(\\d{2})(\\d{5})"), "$1-$2-$3")
