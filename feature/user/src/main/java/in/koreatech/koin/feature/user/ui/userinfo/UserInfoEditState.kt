package `in`.koreatech.koin.feature.user.ui.userinfo

import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.util.ext.isEnglish
import `in`.koreatech.koin.domain.util.ext.isKorean
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.domain.util.ext.isValidEmail
import `in`.koreatech.koin.domain.util.ext.isValidGeneralEmail
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import `in`.koreatech.koin.feature.user.model.NicknameState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState

data class UserInfoEditState(
    val isMajorDropdownExpanded: Boolean = false,
    val verificationCode: String = "",
    val nicknameState: NicknameState = NicknameState.None,
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val phoneNumberState: VerificationMethodState = VerificationMethodState.None,
    val verificationTimeLeft: Int = 180,
    val showWithdrawalDialog: Boolean = false,
    val beforeUserState: UserState = UserState(),
    val userState: UserState = UserState(),
    val userType: UserType = UserType.ANONYMOUS
)

val UserInfoEditState.isPhoneNumberChanged: Boolean
    get() = when (userType) {
        UserType.ANONYMOUS -> false
        UserType.STUDENT, UserType.COUNCIL, UserType.GENERAL -> beforeUserState.phoneNumber != userState.phoneNumber
    }

val UserInfoEditState.isNicknameChanged: Boolean
    get() = when (userType) {
        UserType.ANONYMOUS -> false
        UserType.STUDENT, UserType.COUNCIL, UserType.GENERAL -> beforeUserState.nickname != userState.nickname
    }

val UserInfoEditState.isNameValid: Boolean
    get() = if (userState.name.isKorean()) {
        userState.name.length in 2..5
    } else if (userState.name.isEnglish()) {
        userState.name.length in 2..30
    } else {
        false
    }

val UserInfoEditState.isEmailValid: Boolean
    get() = (userState.email.isNotEmpty() && (userState.email.isValidEmail() || userState.email.isValidGeneralEmail())) || userState.email.isEmpty()

val UserInfoEditState.isNicknameValid
    get() = ((userState.nickname.isNotEmpty() && userState.nickname.isNicknameFormat()) || userState.nickname.isEmpty())

val UserInfoEditState.isStudentNumberValid
    get() = when (userType) {
        UserType.COUNCIL,
        UserType.STUDENT -> userState.studentNumber.isNotEmpty() && userState.studentNumber.isValidStudentId
        UserType.GENERAL -> true
        UserType.ANONYMOUS -> true
    }

val UserInfoEditState.isModified: Boolean
    get() = when (userType) {
        UserType.ANONYMOUS -> false
        UserType.STUDENT, UserType.COUNCIL, UserType.GENERAL -> {
            beforeUserState != userState
        }
    }

private val UserInfoEditState.isNicknameCheckPassed: Boolean
    get() = if (isNicknameChanged) {
        nicknameState is NicknameState.NicknameAvailable
    } else {
        true
    }

val UserInfoEditState.canSave: Boolean
    get() = isModified && isNameValid && isNicknameValid && isNicknameCheckPassed && isStudentNumberValid && isEmailValid && (verificationCodeState is VerificationCodeState.Valid || (verificationCodeState is VerificationCodeState.None && !isPhoneNumberChanged))
