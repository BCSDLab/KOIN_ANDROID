package `in`.koreatech.koin.feature.callvan.ui.report.model

import androidx.compose.runtime.Immutable

@Immutable
data class CallvanReportFirstStepUiState(
    val selectedReason: CallvanReportReason? = null,
    val otherReason: String = "",
    val isOtherReasonError: Boolean = false
)
