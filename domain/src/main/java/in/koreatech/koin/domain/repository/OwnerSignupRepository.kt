package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface OwnerSignupRepository {
    suspend fun getPrivacyTermText(): String
    suspend fun getKoinTermText(): String

    suspend fun requestEmailVerification(
        email: String
    ): Result<Unit>

    suspend fun requestSmsVerificationCode(
        phoneNumber: String,
    ): Pair<Result<Unit>,String?>

    suspend fun getExistsAccount(
        phoneNumber: String
    )
}