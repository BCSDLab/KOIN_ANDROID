package `in`.koreatech.business.feature_signup.accountauth

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class EmailAuthState(
    val authCode: String = "",
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedEmailValidation,
    val signUpContinuationError:Throwable? = null,
    val isLoading: Boolean = false
)