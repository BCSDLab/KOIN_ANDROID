package `in`.koreatech.koin.feature.findpassword.ui.sms

sealed class FindPasswordBySmsSideEffect {
    data object StartTimer : FindPasswordBySmsSideEffect()
    data object StopTimer : FindPasswordBySmsSideEffect()
}