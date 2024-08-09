package `in`.koreatech.business.feature.signup.accountsetup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class AccountSetupState(
    val password: String = "",
    val passwordConfirm: String = "",
    val phoneNumber: String = "",
    val authCode: String = "",
    val isPasswordError: Boolean = false,
    val isPasswordConfirmError: Boolean = false,
    val isPhoneNumberErrorMessage: String? = null,
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedEmailValidation,
    val signUpContinuationError: Throwable? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)