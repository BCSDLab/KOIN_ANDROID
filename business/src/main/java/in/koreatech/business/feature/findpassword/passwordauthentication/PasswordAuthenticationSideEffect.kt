package `in`.koreatech.business.feature.findpassword.passwordauthentication


import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState

sealed class PasswordAuthenticationSideEffect {
    data class GotoChangePasswordScreen(val email: String): PasswordAuthenticationSideEffect()
}

enum class ErrorType {
    NoEmail,
    IsNotEmail,
    NullAuthCode,
    NotCoincideAuthCode
}