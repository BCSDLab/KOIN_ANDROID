package `in`.koreatech.business.feature.signup.accountsetup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class AccountSetupState(
    val id: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val phoneNumber: String = "",
    val authCode: String = "",
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedEmailValidation,
    val signUpContinuationError:Throwable? = null,
    val isLoading: Boolean = false
){
    val isButtonEnabled: Boolean
        get() = id.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty() && phoneNumber.isNotEmpty() && authCode.isNotEmpty()

}