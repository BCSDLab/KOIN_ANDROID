package `in`.koreatech.business.feature.signin

import `in`.koreatech.business.feature.changepassword.passwordauthentication.PasswordAuthenticationSideEffect

sealed class SignInSideEffect {

    object NavigateToSignUp: SignInSideEffect()

    object NavigateToFindPassword: SignInSideEffect()

    object NavigateToMain: SignInSideEffect()

    data class ShowNullMessage(val type: ErrorType): SignInSideEffect()

    data class ShowMessage(val message: String): SignInSideEffect()
}

enum class ErrorType {
    NullEmailOrPassword
}