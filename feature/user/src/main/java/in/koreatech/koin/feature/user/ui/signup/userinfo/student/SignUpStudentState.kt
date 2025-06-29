package `in`.koreatech.koin.feature.user.ui.signup.userinfo.student

import android.os.Parcelable
import `in`.koreatech.koin.domain.util.ext.containsKorean
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpStudentState(
    val phoneNumber: String = "",
    val name: String = "",
    val gender: String = "",
    val loginId: String = "",
    val isLoginIdAvailable: Boolean? = null,
    val isLoginIdValid: Boolean = false,
    val password: String = "",
    val passwordConfirm: String = "",
    val showPassword: Boolean = false,
    val department: String = "",
    val studentNumber: String = "",
    val isDropdownExpanded: Boolean = false,
    val isDepartmentSelected: Boolean = false,
    val nickname: String = "",
    val isNicknameAvailable: Boolean? = null,
    val email: String = "",
    val isEmailAvailable: Boolean? = null,
    val isSignUpSuccess: Boolean = false
) : Parcelable

val SignUpStudentState.isPasswordValid
    get() = password.isValidPassword() && !password.containsKorean()

val SignUpStudentState.isPasswordEqual
    get() = password == passwordConfirm

val SignUpStudentState.currentStep: SignUpStudentStep
    get() = if (isPasswordValid && isPasswordEqual) {
        SignUpStudentStep.NICKNAME_AND_EMAIL
    } else {
        SignUpStudentStep.INITIAL
    }

private val SignUpStudentState.isNicknameValid
    get() = (nickname.isNotEmpty() && nickname.isNicknameFormat() && isNicknameAvailable == true) || nickname.isEmpty()

private val SignUpStudentState.isStudentNumberValid
    get() = studentNumber.isNotEmpty() && studentNumber.isValidStudentId

val SignUpStudentState.isEnabled
    get() = isNicknameValid && isPasswordValid && isPasswordEqual && department.isNotEmpty() && isStudentNumberValid && isLoginIdAvailable == true

enum class SignUpStudentStep {
    INITIAL,
    NICKNAME_AND_EMAIL
}
