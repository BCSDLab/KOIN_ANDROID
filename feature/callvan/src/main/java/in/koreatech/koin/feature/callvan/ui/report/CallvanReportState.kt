package `in`.koreatech.koin.feature.callvan.ui.report

import android.net.Uri
import `in`.koreatech.koin.feature.callvan.ui.report.model.CallvanReportReason
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CallvanReportState(
    val step: Int = 1,
    val selectedReason: CallvanReportReason? = null,
    val otherReason: String = "",
    val detail: String = "",
    val images: ImmutableList<Uri> = persistentListOf(),
    val isLoading: Boolean = false
)
