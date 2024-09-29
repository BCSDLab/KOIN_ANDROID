package `in`.koreatech.business.feature.signin

sealed class SignInSideEffect {

    object NavigateToSignUp: SignInSideEffect()

    object NavigateToFindPassword: SignInSideEffect()

    object NavigateToMyStore: SignInSideEffect()

    object NavigateToRegisterStore: SignInSideEffect()

    data class ShowNullMessage(val type: ErrorType): SignInSideEffect()

    data class ShowMessage(val message: String): SignInSideEffect()
}

enum class ErrorType {
    NullPhoneNumber,
    NullPassword
}