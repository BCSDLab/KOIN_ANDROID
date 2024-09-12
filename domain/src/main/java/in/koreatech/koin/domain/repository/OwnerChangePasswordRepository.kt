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

    suspend fun requestSmsVerification(
        phoneNumber: String,
    )
    suspend fun authenticateSmsCode(
        phoneNumber: String,
        authCode: String,
    ): Result<Unit>
    suspend fun changePasswordSms(
        phoneNumber: String,
        password: String,
    ): Result<Unit>
}