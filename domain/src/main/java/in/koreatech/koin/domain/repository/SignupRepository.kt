package `in`.koreatech.koin.domain.repository

interface SignupRepository {
    suspend fun getPrivacyTermText() : String
    suspend fun getKoinTermText() : String

    suspend fun requestEmailVerification(
        portalAccount: String,
        hashedPassword: String
    ): Result<Unit>
}