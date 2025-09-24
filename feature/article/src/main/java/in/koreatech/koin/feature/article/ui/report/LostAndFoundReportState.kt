package `in`.koreatech.koin.feature.article.ui.report

data class LostAndFoundReportState(
    val selectedReason: IntArray = intArrayOf(),
    val reportReasonDescription: String = ""
)
