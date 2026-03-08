package `in`.koreatech.koin.feature.callvan.ui.report.model

import androidx.compose.runtime.Stable

@Stable
class CallvanReportFirstStepUiState(
    val selectedReason: CallvanReportReason? = null,
    val onSelectedReasonChange: (CallvanReportReason) -> Unit = {},
    val otherReason: String = "",
    val onOtherReasonChange: (String) -> Unit = {}
)
