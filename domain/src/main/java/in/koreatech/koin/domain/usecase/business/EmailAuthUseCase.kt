package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class EmailAuthUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        address: String,
        verificationCode: String
    ): Result<Unit> {
        return try {
            val authToken = ownerVerificationCodeRepository.compareVerificationCode(address, verificationCode)
            tokenRepository.saveOwnerAccessToken(authToken.getOrDefault(defaultValue = null)!!.token)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}