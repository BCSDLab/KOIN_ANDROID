package `in`.koreatech.koin.feature.signup.ui.verification

sealed class SignUpVerificationSideEffect {
    data object StartTimer : SignUpVerificationSideEffect()
    data object StopTimer : SignUpVerificationSideEffect()
}
