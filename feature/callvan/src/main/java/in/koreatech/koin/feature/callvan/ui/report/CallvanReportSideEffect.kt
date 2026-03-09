package `in`.koreatech.koin.feature.callvan.ui.report

sealed class CallvanReportSideEffect {
    data object SubmitSuccess : CallvanReportSideEffect()
    data class ShowErrorMessage(val message: String) : CallvanReportSideEffect()
}
