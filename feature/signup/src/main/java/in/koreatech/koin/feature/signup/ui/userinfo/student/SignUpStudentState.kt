package `in`.koreatech.koin.feature.signup.ui.userinfo.student

import android.os.Parcelable
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpStudentState(
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

val SignUpStudentState.currentStep: SignUpStudentStep
    get() = if (isPasswordValid && isPasswordEqual) {
        SignUpStudentStep.NICKNAME_AND_EMAIL
    } else {
        SignUpStudentStep.INITIAL
    }

private val SignUpStudentState.isEmailValid
    get() = (email.isNotEmpty() && isNicknameAvailable == true) || email.isEmpty()

private val SignUpStudentState.isNicknameValid
    get() = (nickname.isNotEmpty() && nickname.isNicknameFormat() && isNicknameAvailable == true) || nickname.isEmpty()

private val SignUpStudentState.isStudentNumberValid
    get() = studentNumber.isNotEmpty() && studentNumber.isValidStudentId

val SignUpStudentState.isEnabled
    get() = isNicknameValid && isEmailValid && isPasswordValid && isPasswordEqual && department.isNotEmpty() && isStudentNumberValid && isUserIdAvailable == true

enum class SignUpStudentStep {
    INITIAL,
    NICKNAME_AND_EMAIL
}
