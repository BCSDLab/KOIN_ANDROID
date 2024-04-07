package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotBusinessValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class SendSignupEmailUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        passwordConfirm: String,
    ): Result<SignupContinuationState> {
        return when {
            password != passwordConfirm -> Result.success(SignupContinuationState.PasswordNotMatching)
            email.isNotBusinessValidEmail() -> Result.success(SignupContinuationState.EmailIsNotValidate)
            else -> ownerSignupRepository.requestEmailVerification(
                email = email
            ).map { SignupContinuationState.CheckComplete }
        }
    }

}