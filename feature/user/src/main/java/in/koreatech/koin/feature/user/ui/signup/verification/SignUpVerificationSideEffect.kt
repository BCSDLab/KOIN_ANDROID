package `in`.koreatech.koin.feature.user.ui.signup.verification

sealed class SignUpVerificationSideEffect {
    data object StartTimer : SignUpVerificationSideEffect()
    data object StopTimer : SignUpVerificationSideEffect()
}
