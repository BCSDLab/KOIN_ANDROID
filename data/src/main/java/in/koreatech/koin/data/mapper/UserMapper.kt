package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserIdentity

fun UserResponse.toUser() = User(
    id = id,
    isGraduated = isGraduated,
    enabled = enabled,
    anonymousNickname = anonymousNickname,
    portalAccount = portalAccount,
    identity = when (identity) {
        0 -> UserIdentity.Student
        1 -> UserIdentity.Professor
        2 -> UserIdentity.Staff
        else -> UserIdentity.Unknown
    },
    name = name,
    studentNumber = studentNumber,
    profileImageUrl = profileImageUrl,
    gender = when (gender) {
        0 -> Gender.Male
        1 -> Gender.Female
        else -> Gender.Unknown
    },
    nickname = nickname,
    phoneNumber = phoneNumber,
    accountNonExpired = accountNonExpired,
    accountNonLocked = accountNonLocked,
    credentialsNonExpired = credentialsNonExpired,
    username = username,
    major = major
)

fun AuthResponse.toAuthToken() = AuthToken(
    token, user.toUser()
)