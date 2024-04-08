package `in`.koreatech.business.feature_changepassword.passwordauthentication

import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState

sealed class PasswordAuthenticationSideEffect {
    data class GotoChangePasswordScreen(val email: String): PasswordAuthenticationSideEffect()

    object SendAuthCode: PasswordAuthenticationSideEffect()
    object ToastNoEmail: PasswordAuthenticationSideEffect()

    object ToastIsNotEmail: PasswordAuthenticationSideEffect()

    object ToastNullAuthCode: PasswordAuthenticationSideEffect()
    object ToastNotCoincideAuthCode: PasswordAuthenticationSideEffect()

}