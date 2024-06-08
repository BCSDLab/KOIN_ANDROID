package `in`.koreatech.business.feature.signup.accountsetup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class AccountSetupState(
    val id: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val phoneNumber: String = "",
    val authCode: String = "",
    val isPasswordError: Boolean = false,
    val isPasswordConfirmError: Boolean = false,
    val isPhoneNumberError: Boolean = false,
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedEmailValidation,
    val signUpContinuationError: Throwable? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)