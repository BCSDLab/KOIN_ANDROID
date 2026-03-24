package `in`.koreatech.koin.feature.callvan.ui.create

import `in`.koreatech.koin.feature.callvan.ui.create.model.SubmitErrorType

sealed interface CallvanCreateSideEffect {
    data object NavigateToMain : CallvanCreateSideEffect
    data class ShowSubmitError(val type: SubmitErrorType) : CallvanCreateSideEffect
    data object ShowPastTimeError : CallvanCreateSideEffect
}
