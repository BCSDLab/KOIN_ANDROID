package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class OwnerVerificationCodeUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): Result<SignupContinuationState> {
        return try{
            val authToken = ownerVerificationCodeRepository.verifySmsCode(
                phoneNumber,
                verificationCode
            )
            authToken.getOrDefault(defaultValue = null)
                ?.let { tokenRepository.saveOwnerAccessToken(it.token) }
            if (authToken.isFailure) {
                Result.failure(authToken.exceptionOrNull() ?: CancellationException())
            } else
                Result.success(SignupContinuationState.CheckComplete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
