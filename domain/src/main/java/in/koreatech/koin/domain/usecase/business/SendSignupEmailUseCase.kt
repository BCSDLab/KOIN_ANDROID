package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotBusinessValidEmail
import javax.inject.Inject

class SendSignupEmailUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        passwordConfirm: String,
    ): Result<SignupContinuationState> {
        return when {
            password != passwordConfirm -> Result.success(SignupContinuationState.PasswordNotMatching)
            email.trim().isNotBusinessValidEmail() -> Result.success(SignupContinuationState.EmailIsNotValidate)
            else -> Result.success(SignupContinuationState.CheckComplete)
        }
    }

    suspend fun sendEmail(
        email: String,
    ): Result<Unit> {
        return ownerSignupRepository.requestEmailVerification(
            email = email
        ).map { SignupContinuationState.CheckComplete }
    }



}