package `in`.koreatech.koin.domain.repository

interface OwnerSignupRepository {
    suspend fun getPrivacyTermText(): String
    suspend fun getKoinTermText(): String

    suspend fun requestEmailVerification(
        email: String
    ): Result<Unit>

    suspend fun requestSmsVerificationCode(
        phoneNumber: String,
    ): Result<Unit>

    suspend fun getExistsAccount(
        phoneNumber: String
    ): Result<Unit>
}