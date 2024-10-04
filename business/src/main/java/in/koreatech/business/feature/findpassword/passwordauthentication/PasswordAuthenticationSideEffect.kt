package `in`.koreatech.business.feature.findpassword.passwordauthentication

sealed class PasswordAuthenticationSideEffect {
    data class GotoChangePasswordScreen(val email: String): PasswordAuthenticationSideEffect()
}

enum class ErrorType {
    NoEmail,
    IsNotEmail,
    NullAuthCode,
    NotCoincideAuthCode
}