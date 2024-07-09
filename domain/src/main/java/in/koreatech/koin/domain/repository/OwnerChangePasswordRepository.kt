package `in`.koreatech.koin.domain.repository

interface OwnerChangePasswordRepository  {
    suspend fun requestEmailVerification(
        email: String
    ): Result<Unit>

    suspend fun authenticateCode(
        email: String,
        authCode: String
    ): Result<Unit>

    suspend fun changePassword(
        email: String,
        password: String
    ): Result<Unit>

}