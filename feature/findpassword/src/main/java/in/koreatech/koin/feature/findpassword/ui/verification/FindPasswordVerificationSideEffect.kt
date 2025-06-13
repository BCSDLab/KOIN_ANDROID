package `in`.koreatech.koin.feature.findpassword.ui.verification

sealed class FindPasswordVerificationSideEffect {
    data object StartTimer : FindPasswordVerificationSideEffect()
    data object StopTimer : FindPasswordVerificationSideEffect()
    data object NavigateToChangePassword : FindPasswordVerificationSideEffect()
    data object UnknownError : FindPasswordVerificationSideEffect()
}
