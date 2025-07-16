package `in`.koreatech.koin.feature.user.ui.findpassword.verification

sealed class FindPasswordVerificationSideEffect {
    data object StartTimer : FindPasswordVerificationSideEffect()
    data object StopTimer : FindPasswordVerificationSideEffect()
    data object NavigateToChangePassword : FindPasswordVerificationSideEffect()
    data object UnknownError : FindPasswordVerificationSideEffect()
}
