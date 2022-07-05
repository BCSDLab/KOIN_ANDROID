package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.AuthToken

interface UserRepository {
    suspend fun getToken(
        portalAccount: String,
        hashedPassword: String
    ): AuthToken
}