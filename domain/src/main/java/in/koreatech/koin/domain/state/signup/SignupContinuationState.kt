package `in`.koreatech.koin.domain.state.signup

sealed class SignupContinuationState {
    object RequestedEmailValidation: SignupContinuationState()
    object EmailIsNotValidate: SignupContinuationState()
    object PasswordIsNotValidate: SignupContinuationState()
    object PasswordNotMatching: SignupContinuationState()
    object NotAgreedPrivacyTerms: SignupContinuationState()
    object NotAgreedKoinTerms: SignupContinuationState()
}
