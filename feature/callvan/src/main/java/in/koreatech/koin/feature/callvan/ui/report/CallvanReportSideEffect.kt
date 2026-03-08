package `in`.koreatech.koin.feature.callvan.ui.report

sealed class CallvanReportSideEffect {
    data object NavigateBack : CallvanReportSideEffect()
    data class ShowErrorMessage(val message: String) : CallvanReportSideEffect()
}