package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.user.UserRequest
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
        0 -> Gender.Man
        1 -> Gender.Woman
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

fun User.toUserRequest() = UserRequest(
    id = id,
    portalAccount = portalAccount,
    nickname = nickname,
    anonymousNickname = anonymousNickname,
    name = name,
    studentNumber = studentNumber,
    major = major,
    identity = when (identity) {
        UserIdentity.Student -> 0
        UserIdentity.Professor -> 1
        UserIdentity.Staff -> 2
        UserIdentity.Unknown -> 0
    },
    isGraduated = isGraduated,
    phoneNumber = phoneNumber,
    gender = when (gender) {
        Gender.Man -> 0
        Gender.Woman -> 1
        else -> null
    },
    profileImageUrl = profileImageUrl
)

fun AuthResponse.toAuthToken() = AuthToken(
    token, user.toUser()
)