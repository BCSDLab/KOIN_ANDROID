package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User

interface UserRepository {
    suspend fun getToken(
        portalAccount: String,
        hashedPassword: String,
    ): AuthToken

    suspend fun getUserInfo(): User

    suspend fun requestPasswordResetEmail(email: String)

    suspend fun deleteUser()
    suspend fun isUsernameDuplicated(nickname: String): Boolean
    suspend fun isUserEmailDuplicated(email: String): Boolean
    suspend fun updateUser(user: User)
    suspend fun updateDeviceToken(token: String)
    suspend fun deleteDeviceToken()
    suspend fun verifyPassword(hashedPassword: String)
}