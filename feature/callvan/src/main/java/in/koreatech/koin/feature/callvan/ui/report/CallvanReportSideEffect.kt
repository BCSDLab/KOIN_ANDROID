package `in`.koreatech.koin.feature.callvan.ui.report

import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportErrorType

sealed class CallvanReportSideEffect {
    data object SubmitSuccess : CallvanReportSideEffect()
    data class ShowErrorMessage(val errorType: CallvanReportErrorType) : CallvanReportSideEffect()
}
