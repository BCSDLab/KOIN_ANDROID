package `in`.koreatech.koin.feature.user.ui.signin

sealed class SignInSideEffect {
    data object SignInSuccess : SignInSideEffect()
}
