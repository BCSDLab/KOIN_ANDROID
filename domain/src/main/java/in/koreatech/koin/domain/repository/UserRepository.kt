package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User

interface UserRepository {
    suspend fun getToken(
        portalAccount: String,
        hashedPassword: String
    ): AuthToken

    suspend fun getUserInfo(): User

    suspend fun requestPasswordResetEmail(
        portalAccount: String
    )

    suspend fun deleteUser()
}