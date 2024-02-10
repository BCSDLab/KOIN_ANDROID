package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class SignupRequestEmailVerificationUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ): Result<SignupContinuationState> {
        return when {
            portalAccount.isNotValidEmail() -> Result.success(SignupContinuationState.EmailIsNotValidate)
            password.isNotValidPassword() -> Result.success(SignupContinuationState.PasswordIsNotValidate)
            password != passwordConfirm -> Result.success(SignupContinuationState.PasswordNotMatching)
            !isAgreedPrivacyTerms -> Result.success(SignupContinuationState.NotAgreedPrivacyTerms)
            !isAgreedKoinTerms -> Result.success(SignupContinuationState.NotAgreedKoinTerms)
            else -> signupRepository.requestEmailVerification(
                email = portalAccount,
                password = password.toSHA256()
            ).map {
                SignupContinuationState.RequestedEmailValidation
            }
        }
    }
}