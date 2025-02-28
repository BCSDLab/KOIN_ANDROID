package `in`.koreatech.koin.feature.lostandfound.ui.report

import `in`.koreatech.koin.feature.lostandfound.enums.ReportReason

data class LostAndFoundReportState(
    val reportReason: ReportReason? = null,
    val reportReasonTitle: String = "",
    val reportReasonDescription: String = "",
)
