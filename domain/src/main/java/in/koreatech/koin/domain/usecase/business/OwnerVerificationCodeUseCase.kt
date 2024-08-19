package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class OwnerVerificationCodeUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
    private val ownerErrorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = ownerVerificationCodeRepository.verifySmsCode(
                phoneNumber,
                verificationCode
            )
            tokenRepository.saveOwnerAccessToken(authToken.token)
            Result.success(SignupContinuationState.CheckComplete)
            Unit to null
        } catch (t: Throwable) {
            null to ownerErrorHandler.handleVerifySmsCodeError(t)
        }
    }
}
