package `in`.koreatech.koin.feature.findpassword.ui.email

sealed class FindPasswordByEmailSideEffect {
    data object StartTimer : FindPasswordByEmailSideEffect()
    data object StopTimer : FindPasswordByEmailSideEffect()
}
