package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.feature.userinfo.model.PhoneNumberState
import `in`.koreatech.koin.feature.userinfo.model.VerificationCodeState

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
    val verificationCodeState: VerificationCodeState = VerificationCodeState.None,
    val phoneNumberState: PhoneNumberState = PhoneNumberState.None,
    val verificationTimeLeft: Int = 180,
    val showWithdrawalDialog: Boolean = false,
) {
    val isPhoneNumberChanged: Boolean
        get() = when (beforeUser) {
            User.Anonymous -> false
            is User.Student -> beforeUser.phoneNumber != phoneNumber.ifEmpty { null }
            is User.General -> beforeUser.phoneNumber != phoneNumber
        }

    val isNicknameChanged: Boolean
        get() = when (beforeUser) {
            User.Anonymous -> false
            is User.Student -> beforeUser.nickname != nickname.ifEmpty { null }
            is User.General -> beforeUser.nickname != nickname.ifEmpty { null }
        }

    val isModified: Boolean
        get() = when (beforeUser) {
            User.Anonymous -> false
            is User.Student -> {
                beforeUser.loginId != loginId || beforeUser.name != name.ifEmpty { null } ||
                        beforeUser.nickname != nickname.ifEmpty { null } || beforeUser.phoneNumber != phoneNumber.ifEmpty { null } ||
                        beforeUser.email != email || beforeUser.gender != gender ||
                        beforeUser.studentNumber != studentNumber.ifEmpty { null } || beforeUser.major != major.ifEmpty { null }
            }

            is User.General -> {
                beforeUser.loginId != loginId || beforeUser.name != name ||
                        beforeUser.nickname != nickname.ifEmpty { null } || beforeUser.phoneNumber != phoneNumber ||
                        beforeUser.email != email || beforeUser.gender != gender
            }
        }
}
