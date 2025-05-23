package `in`.koreatech.koin.feature.signin.ui

sealed class SignInSideEffect {
    data object SignInSuccess : SignInSideEffect()
}
