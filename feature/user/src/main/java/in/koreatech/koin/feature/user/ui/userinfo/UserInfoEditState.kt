package `in`.koreatech.koin.feature.user.ui.userinfo

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
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
    val beforeUser: User = User.Anonymous,
    val loginId: String = "",
    val anonymousNickname: String = "",
    val email: String = "",
    val gender: Gender = Gender.Unknown,
    val name: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val studentNumber: String = "",
    val major: String = "",
    val isMajorDropdownExpanded: Boolean = false,
    val userType: UserType = UserType.ANONYMOUS,
    val verificationCode: String = "",
    val nicknameState: NicknameState = NicknameState.None,
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val phoneNumberState: VerificationMethodState = VerificationMethodState.None,
    val verificationTimeLeft: Int = 180,
    val showWithdrawalDialog: Boolean = false
)

val UserInfoEditState.isPhoneNumberChanged: Boolean
    get() = when (beforeUser) {
        User.Anonymous -> false
        is User.Student -> beforeUser.phoneNumber != phoneNumber.ifEmpty { null }
        is User.General -> beforeUser.phoneNumber != phoneNumber
    }

val UserInfoEditState.isNicknameChanged: Boolean
    get() = when (beforeUser) {
        User.Anonymous -> false
        is User.Student -> beforeUser.nickname != nickname.ifEmpty { null }
        is User.General -> beforeUser.nickname != nickname.ifEmpty { null }
    }

val UserInfoEditState.isNameValid: Boolean
    get() = if (name.isKorean()) {
        name.length in 2..5
    } else if (name.isEnglish()) {
        name.length in 2..30
    } else {
        false
    }

val UserInfoEditState.isEmailValid: Boolean
    get() = (email.isNotEmpty() && (email.isValidEmail() || email.isValidGeneralEmail())) || email.isEmpty()

val UserInfoEditState.isNicknameValid
    get() = ((nickname.isNotEmpty() && nickname.isNicknameFormat()) || nickname.isEmpty())

val UserInfoEditState.isStudentNumberValid
    get() = when (userType) {
        UserType.COUNCIL,
        UserType.STUDENT -> studentNumber.isNotEmpty() && studentNumber.isValidStudentId
        UserType.GENERAL -> true
        UserType.ANONYMOUS -> true
    }

val UserInfoEditState.isModified: Boolean
    get() = when (beforeUser) {
        User.Anonymous -> false
        is User.Student -> {
            beforeUser.loginId != loginId || beforeUser.name != name.ifEmpty { null } ||
                beforeUser.nickname != nickname.ifEmpty { null } || beforeUser.phoneNumber != phoneNumber.ifEmpty { null } ||
                beforeUser.email != email.ifEmpty { null } || beforeUser.gender != gender ||
                beforeUser.studentNumber != studentNumber.ifEmpty { null } || beforeUser.major != major.ifEmpty { null }
        }

        is User.General -> {
            beforeUser.loginId != loginId || beforeUser.name != name ||
                beforeUser.nickname != nickname.ifEmpty { null } || beforeUser.phoneNumber != phoneNumber ||
                beforeUser.email != email.ifEmpty { null } || beforeUser.gender != gender
        }
    }

val UserInfoEditState.canSave: Boolean
    get() = isModified && isNameValid && isNicknameValid && nicknameState is NicknameState.NicknameAvailable && isStudentNumberValid && isEmailValid && (verificationCodeState is VerificationCodeState.Valid || verificationCodeState is VerificationCodeState.None)
