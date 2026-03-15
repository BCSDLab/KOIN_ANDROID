package `in`.koreatech.koin.feature.callvan.ui.report.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CallvanReportFirstStepUiState(
    val selectedReasons: ImmutableList<CallvanReportReason> = persistentListOf(),
    val otherReason: String = "",
    val isOtherReasonError: Boolean = false
)
