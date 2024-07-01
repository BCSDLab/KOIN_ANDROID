package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class SendSignupSmsCodeUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
    ): Result<SignupContinuationState> {
        return try {
            ownerSignupRepository.requestSmsVerificationCode(phoneNumber)
            Result.success(SignupContinuationState.RequestedSmsValidation)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}