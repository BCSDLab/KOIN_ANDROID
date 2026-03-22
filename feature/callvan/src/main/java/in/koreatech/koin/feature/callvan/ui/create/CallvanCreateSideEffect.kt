package `in`.koreatech.koin.feature.callvan.ui.create

sealed interface CallvanCreateSideEffect {
    data object NavigateToMain : CallvanCreateSideEffect
    data object ShowSubmitError : CallvanCreateSideEffect
    data object ShowPastTimeError : CallvanCreateSideEffect
}
