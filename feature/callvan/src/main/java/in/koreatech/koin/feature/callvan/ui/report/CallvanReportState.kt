package `in`.koreatech.koin.feature.callvan.ui.report

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CallvanReportState(
    val step: Int = 1,
    val selectedReasons: ImmutableList<CallvanReportReason> = persistentListOf(),
    val otherReason: String = "",
    val isOtherReasonError: Boolean = false,
    val detail: String = "",
    val images: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false
)
