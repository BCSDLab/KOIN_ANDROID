package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class OwnerVerificationCodeUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        address: String,
        verificationCode: String
    ): Pair<Unit?, Result<Unit>> {
        return try {
            val authToken = ownerVerificationCodeRepository.compareVerificationCode(address, verificationCode).first
            tokenRepository.saveOwnerAccessToken(authToken!!.token)
            Unit to Result.success(Unit)
        } catch (throwable: Throwable) {
            null to Result.failure(throwable)
        }
    }
}