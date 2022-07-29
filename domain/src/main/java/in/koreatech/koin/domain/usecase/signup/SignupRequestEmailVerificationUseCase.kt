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

            else -> try {
                signupRepository.requestEmailVerification(
                    portalAccount = portalAccount,
                    hashedPassword = password.toSHA256()
                )
                Result.success(SignupContinuationState.RequestedEmailValidation)
            } catch (t: Throwable) {
                if(t.message == "HTTP 409 ") { // 이메일 인증을 한 경우
                    Result.success(SignupContinuationState.AlreadySentEmailValidation)
                } else {
                    Result.failure(t)
                }
            }
        }
    }
}