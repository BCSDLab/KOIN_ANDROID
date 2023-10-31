package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.toSHA256
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
            email.isNotValidEmail() -> Result.success(SignupContinuationState.EmailIsNotValidate)
            password.isNotValidPassword() -> Result.success(SignupContinuationState.PasswordIsNotValidate)
            password != passwordConfirm -> Result.success(SignupContinuationState.PasswordNotMatching)
            !isAgreedPrivacyTerms -> Result.success(SignupContinuationState.NotAgreedPrivacyTerms)
            !isAgreedKoinTerms -> Result.success(SignupContinuationState.NotAgreedKoinTerms)
            else -> ownerSignupRepository.requestEmailVerification(
                email = email
            ).map { SignupContinuationState.RequestedEmailValidation }
        }
    }
}