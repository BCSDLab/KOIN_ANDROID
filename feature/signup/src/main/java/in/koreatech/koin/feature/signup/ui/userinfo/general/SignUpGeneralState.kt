package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import android.os.Parcelable
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
    val isPasswordValid: Boolean = false,
    val isPasswordEqual: Boolean = false,
    val showPassword: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isSignUpSuccess: Boolean = false
) : Parcelable

val SignUpGeneralState.currentStep: SignUpGeneralStep
    get() = if (isPasswordValid && isPasswordEqual) {
        SignUpGeneralStep.NICKNAME_AND_EMAIL
    } else {
        SignUpGeneralStep.INITIAL
    }

val SignUpGeneralState.isEnabled
    get() = (nickname.isNotEmpty() && isNicknameAvailable == true || nickname.isEmpty()) && isPasswordValid && isPasswordEqual && isLoginIdAvailable == true

enum class SignUpGeneralStep {
    INITIAL,
    NICKNAME_AND_EMAIL
}
