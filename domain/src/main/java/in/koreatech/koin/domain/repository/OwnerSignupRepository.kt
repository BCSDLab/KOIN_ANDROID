package `in`.koreatech.koin.domain.repository

interface OwnerSignupRepository {
    suspend fun getPrivacyTermText(): String
    suspend fun getKoinTermText(): String

    suspend fun requestEmailVerification(
        email: String,
        hashedPassword: String
    ): Result<Unit>
}