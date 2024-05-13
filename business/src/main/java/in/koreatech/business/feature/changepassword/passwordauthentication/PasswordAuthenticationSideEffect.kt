package `in`.koreatech.business.feature.changepassword.passwordauthentication

sealed class PasswordAuthenticationSideEffect {
    data class GotoChangePasswordScreen(val email: String): PasswordAuthenticationSideEffect()

    object SendAuthCode: PasswordAuthenticationSideEffect()

    data class ShowMessage(val type: ErrorType): PasswordAuthenticationSideEffect()
}

enum class ErrorType {
    NoEmail,
    IsNotEmail,
    NullAuthCode,
    NotCoincideAuthCode
}