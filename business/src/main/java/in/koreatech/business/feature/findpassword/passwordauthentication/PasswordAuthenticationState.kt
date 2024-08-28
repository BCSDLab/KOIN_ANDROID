package `in`.koreatech.business.feature.findpassword.passwordauthentication

import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState

data class PasswordAuthenticationState (
    val authenticationBtnIsClicked: Boolean = false,
    val phoneNumber: String = "",
    val authenticationCode: String = "",
    val accountContinuationState: ChangePasswordContinuationState=ChangePasswordContinuationState.RequestedSmsValidation,
    val smsAuthContinuationState: ChangePasswordContinuationState=ChangePasswordContinuationState.RequestedSmsValidation,
    val sendSmsError:Throwable?=null,
    val authError:Throwable?=null,
    )

fun PasswordAuthenticationState.phoneNumberIsEmpty() = phoneNumber.isEmpty()

fun PasswordAuthenticationState.authCodeIsEmpty() = authenticationCode.isEmpty()
