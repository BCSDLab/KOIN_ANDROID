package `in`.koreatech.koin.feature.article.ui.report

sealed class LostAndFoundReportSideEffect {
    data object ReportSuccess : LostAndFoundReportSideEffect()

    data class ReportFailure(val reason: String) : LostAndFoundReportSideEffect()
}
