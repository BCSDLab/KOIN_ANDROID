package `in`.koreatech.koin.feature.signup.ui.userinfo.student

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpStudentState(
    val step: SignUpStudentStep = SignUpStudentStep.INITIAL,
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
    val department: String = "",
    val studentNumber: String = "",
    val isDropdownExpanded: Boolean = false,
    val isDepartmentSelected: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isSignUpSuccess: Boolean = false
) : Parcelable

val SignUpStudentState.isEnabled
    get() = (nickname.isNotEmpty() && isNicknameAvailable == true || nickname.isEmpty()) && isPasswordValid && isPasswordEqual && department.isNotEmpty() && studentNumber.isNotEmpty() && isUserIdAvailable == true

enum class SignUpStudentStep {
    INITIAL,
    NICKNAME_AND_EMAIL
}
