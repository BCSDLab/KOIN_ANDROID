package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class OwnerSignupRequestEmailVerificationUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ): Result<SignupContinuationState> {
        return when {
            email.isNotValidEmail() -> Result.success(SignupContinuationState.EmailInvalid)
            password.isNotValidPassword() -> Result.success(SignupContinuationState.PasswordInvalid)
            password != passwordConfirm -> Result.success(SignupContinuationState.PasswordNotMatching)
            !isAgreedPrivacyTerms -> Result.success(SignupContinuationState.PrivacyTermsNotAgreed)
            !isAgreedKoinTerms -> Result.success(SignupContinuationState.KoinTermsNotAgreed)
            else ->
                ownerSignupRepository.requestEmailVerification(
                    email = email
                ).map { SignupContinuationState.EmailValidationRequested }
        }
    }
}
