package `in`.koreatech.koin.feature.callvan.ui.report.model

class CallvanReportFirstStepUiAction(
    val onSelectedReasonChange: (CallvanReportReason) -> Unit = {},
    val onOtherReasonChange: (String) -> Unit = {}
)
