package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import javax.inject.Inject
class SmsVerificationUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): Result<SignupContinuationState> {
        return try {
            val authToken = ownerVerificationCodeRepository.verifySmsCode(phoneNumber.formatPhoneNumber(), verificationCode)
            authToken.getOrDefault(defaultValue = null)
                ?.let { tokenRepository.saveOwnerAccessToken(it.token) }
            Result.success(SignupContinuationState.CheckComplete)
        } catch (t: Exception) {
            Result.failure(t)
        }
    }
}