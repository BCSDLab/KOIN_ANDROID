package `in`.koreatech.business.feature.signup.accountsetup

import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class AccountSetupState(
    val password: String = "",
    val passwordConfirm: String = "",
    val phoneNumber: String = "",
    val authCode: String = "",
    val isPasswordError: Boolean = false,
    val isPasswordConfirmError: Boolean = false,
    val verifyState : SignupContinuationState = SignupContinuationState.AvailablePhoneNumber,
    val phoneNumberState: SignupContinuationState = SignupContinuationState.AvailablePhoneNumber,
    val verifyError:Throwable? = null,
    val sendCodeError:Throwable? = null,
    val isButtonEnabled: Boolean = false
)