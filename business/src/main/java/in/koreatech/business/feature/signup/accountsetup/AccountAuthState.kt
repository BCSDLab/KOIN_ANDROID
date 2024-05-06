package `in`.koreatech.business.feature.signup.accountsetup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class AccountAuthState(
    val id: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val email: String = "",
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedEmailValidation,
    val signUpContinuationError:Throwable? = null,
    val isLoading: Boolean = false
){
    val isPasswordEnabled: Boolean
        get() = id.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty() && email.isNotEmpty()
}