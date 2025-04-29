package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpGeneralState(
    val step: SignUpGeneralStep = SignUpGeneralStep.INITIAL,
    val phoneNumber: String = "",
    val name: String = "",
    val gender: String = "",
    val userId: String = "",
    val isUserIdAvailable: Boolean? = null,
    val isUserIdValid: Boolean = false,
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

val SignUpGeneralState.isEnabled
    get() = (nickname.isNotEmpty() && isNicknameAvailable == true || nickname.isEmpty()) && isPasswordValid && isPasswordEqual && isUserIdAvailable == true

enum class SignUpGeneralStep {
    INITIAL,
    NICKNANE_AND_EMAIL
}
