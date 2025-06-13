package `in`.koreatech.koin.feature.findpassword.ui.verification

sealed class FindPasswordByVerificationSideEffect {
    data object StartTimer : FindPasswordByVerificationSideEffect()
    data object StopTimer : FindPasswordByVerificationSideEffect()
    data object NavigateToChangePassword : FindPasswordByVerificationSideEffect()
    data object UnknownError : FindPasswordByVerificationSideEffect()
}
