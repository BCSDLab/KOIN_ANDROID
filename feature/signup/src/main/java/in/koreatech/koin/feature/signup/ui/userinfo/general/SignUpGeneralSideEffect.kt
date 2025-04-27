package `in`.koreatech.koin.feature.signup.ui.userinfo.general

sealed class SignUpGeneralSideEffect {
    data object SignUpSuccess : SignUpGeneralSideEffect()
    data object SignUpFailure : SignUpGeneralSideEffect()
}
