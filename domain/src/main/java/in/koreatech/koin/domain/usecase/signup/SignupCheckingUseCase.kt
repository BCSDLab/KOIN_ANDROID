package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class SignupCheckingUseCase @Inject constructor() {
    operator fun invoke(
        portalAccount: String,
        password: String,
        passwordConfirm: String,
        isAgreedPrivacyTerms: Boolean,
        isAgreedKoinTerms: Boolean
    ): SignupContinuationState {
        return when {
            (portalAccount.isNotValidEmail() || portalAccount.contains(" ")) -> SignupContinuationState.EmailInvalid
            (password.isNotValidPassword() || password.contains(" ")) -> SignupContinuationState.PasswordInvalid
            password != passwordConfirm -> SignupContinuationState.PasswordNotMatching
            !isAgreedPrivacyTerms -> SignupContinuationState.PrivacyTermsNotAgreed
            !isAgreedKoinTerms -> SignupContinuationState.KoinTermsNotAgreed
            else -> SignupContinuationState.SignupCheckComplete
        }
    }
}
