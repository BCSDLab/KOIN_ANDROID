package `in`.koreatech.koin.feature.lostandfound.ui.report

sealed class LostAndFoundReportSideEffect {
    data object ReportSuccess : LostAndFoundReportSideEffect()
    data class ReportFailure(val reason: String) : LostAndFoundReportSideEffect()
}