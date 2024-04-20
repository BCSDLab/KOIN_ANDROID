package `in`.koreatech.business.feature_changepassword.passwordauthentication

sealed class PasswordAuthenticationSideEffect {
    data class AuthenticationBtnIsClicked(val authenticationBtnIsClicked: Boolean): PasswordAuthenticationSideEffect()
    object GotoChangePasswordScreen: PasswordAuthenticationSideEffect()
    object ToastNoEmail: PasswordAuthenticationSideEffect()
}