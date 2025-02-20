package `in`.koreatech.business.feature.event.writeevent.writeevent

import android.net.Uri

data class WriteEventState(
    val title: String = "",
    val content: String = "",
    val startDate: String = "2025-01-01",
    val endDate: String = "2025-12-31",
    val startYear: String = "",
    val startMonth: String = "",
    val startDay: String = "",
    val endYear: String = "",
    val endMonth: String = "",
    val endDay: String = "",
    val images: List<Uri> = emptyList(),
    val showTitleInputAlert: Boolean = false,
    val showContentInputAlert: Boolean = false,
    val showDateInputAlert: Boolean = false,
)