package `in`.koreatech.koin.feature.user.ui.findid.verification

sealed class FindIdVerificationSideEffect {
    data class NavigateToCompleteScreen(val loginId: String) : FindIdVerificationSideEffect()
    data object StartTimer : FindIdVerificationSideEffect()
    data object StopTimer : FindIdVerificationSideEffect()
}
