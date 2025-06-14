package `in`.koreatech.koin.feature.findid.ui.verification

sealed class FindIdVerificationSideEffect {
    data class NavigateToCompleteScreen(val loginId: String) : FindIdVerificationSideEffect()
    data object StartTimer : FindIdVerificationSideEffect()
    data object StopTimer : FindIdVerificationSideEffect()
}
