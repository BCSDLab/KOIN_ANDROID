package `in`.koreatech.business.feature_changepassword.passwordauthentication

import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState

data class PasswordAuthenticationState (
    val authenticationBtnIsClicked: Boolean = false,
    val phoneNumber: String = "",
    val authenticationCode: String = "",
    val accountContinuationState: ChangePasswordContinuationState=ChangePasswordContinuationState.RequestedSmsValidation,
    val smsAuthContinuationState: ChangePasswordContinuationState=ChangePasswordContinuationState.RequestedSmsValidation,
    val authErrorMessage:String?= null,
    )

fun PasswordAuthenticationState.phoneNumberIsEmpty() = phoneNumber.isEmpty()

fun PasswordAuthenticationState.authCodeIsEmpty() = authenticationCode.isEmpty()
