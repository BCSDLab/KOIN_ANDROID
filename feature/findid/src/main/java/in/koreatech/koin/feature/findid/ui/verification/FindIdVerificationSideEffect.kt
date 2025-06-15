package `in`.koreatech.koin.feature.findid.ui.verification

sealed class FindIdVerificationSideEffect {
    data object NavigateToCompleteScreen : FindIdVerificationSideEffect()
    data object StartTimer : FindIdVerificationSideEffect()
    data object StopTimer : FindIdVerificationSideEffect()
}
