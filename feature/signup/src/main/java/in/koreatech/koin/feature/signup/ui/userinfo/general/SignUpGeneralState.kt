package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import android.os.Parcelable
import `in`.koreatech.koin.domain.util.ext.containsKorean
import `in`.koreatech.koin.domain.util.ext.isValidGeneralEmail
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpGeneralState(
    val phoneNumber: String = "",
    val name: String = "",
    val gender: String = "",
    val loginId: String = "",
    val isLoginIdAvailable: Boolean? = null,
    val isLoginIdValid: Boolean = false,
    val password: String = "",
    val passwordConfirm: String = "",
    val isPasswordEqual: Boolean = false,
    val showPassword: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isEmailAvailable: Boolean? = null,
    val isSignUpSuccess: Boolean = false
) : Parcelable

val SignUpGeneralState.isPasswordValid
    get() = password.isValidPassword() && !password.containsKorean()

val SignUpGeneralState.currentStep: SignUpGeneralStep
    get() = if (isPasswordValid && isPasswordEqual) {
        SignUpGeneralStep.NICKNAME_AND_EMAIL
    } else {
        SignUpGeneralStep.INITIAL
    }

private val SignUpGeneralState.isEmailValid
    get() = (email.isNotEmpty() && email.isValidGeneralEmail()) || email.isEmpty()

private val SignUpGeneralState.isNicknameValid
    get() = (nickname.isNotEmpty() && isNicknameAvailable == true) || nickname.isEmpty()

val SignUpGeneralState.isEnabled
    get() = isNicknameValid && isEmailValid && isPasswordValid && isPasswordEqual && isLoginIdAvailable == true

enum class SignUpGeneralStep {
    INITIAL,
    NICKNAME_AND_EMAIL
}
