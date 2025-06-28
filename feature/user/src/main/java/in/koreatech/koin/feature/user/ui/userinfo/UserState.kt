package `in`.koreatech.koin.feature.user.ui.userinfo

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType

data class UserState(
    val id: Int = 0,
    val loginId: String = "",
    val anonymousNickname: String = "",
    val email: String = "",
    val gender: Gender = Gender.Unknown,
    val name: String = "",
    val nickname: String = "",
    val phoneNumber: String = "",
    val studentNumber: String = "",
    val major: String = ""
)

fun UserState.toUser(userType: UserType): User = when (userType) {
    UserType.ANONYMOUS -> User.Anonymous
    UserType.STUDENT, UserType.COUNCIL -> User.Student(
        id = id,
        loginId = loginId,
        email = email,
        anonymousNickname = anonymousNickname,
        gender = gender,
        major = major,
        name = name,
        nickname = nickname,
        phoneNumber = phoneNumber,
        studentNumber = studentNumber,
        userType = userType.name
    )

    UserType.GENERAL -> User.General(
        id = id,
        loginId = loginId,
        email = email,
        anonymousNickname = anonymousNickname,
        gender = gender,
        name = name,
        nickname = nickname,
        phoneNumber = phoneNumber,
        userType = userType.name
    )
}
