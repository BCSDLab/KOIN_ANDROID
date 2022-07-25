package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User

fun UserResponse.toUser() = User(
    this.credentialsNonExpired,
    this.isGraduated,
    this.enabled,
    this.anonymousNickname,
    this.portalAccount,
    this.identity,
    this.name,
    this.nickname,
    this.accountNonExpired,
    this.id,
    this.accountNonLocked,
    this.username
)

fun AuthResponse.toAuthToken() = AuthToken(
    this.token, this.user.toUser()
)