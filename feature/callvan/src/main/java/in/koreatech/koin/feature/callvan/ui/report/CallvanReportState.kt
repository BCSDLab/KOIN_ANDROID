package `in`.koreatech.koin.feature.callvan.ui.report

import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanReportState(
    val step: Int = 1,
    val selectedReason: CallvanReportReason? = null,
    val otherReason: String = "",
    val isOtherReasonError: Boolean = false,
    val detail: String = "",
    val images: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false
)
