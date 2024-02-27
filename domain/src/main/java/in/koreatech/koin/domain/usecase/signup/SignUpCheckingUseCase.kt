package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class SignUpCheckingUseCase @Inject constructor(

){
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
            else -> {Result.success(SignupContinuationState.CheckComplete)}
        }
    }
}