package `in`.koreatech.koin.domain.repository


interface OwnerVerificationCodeRepository {
    suspend fun compareVerificationCode(
        address: String,
        verificationCode: String
    ): Result<Unit>
}