package `in`.koreatech.koin.feature.article.ui.lostandfound.report

sealed class LostAndFoundReportSideEffect {
    data object ReportSuccess : LostAndFoundReportSideEffect()

    data class ReportFailure(val reason: String) : LostAndFoundReportSideEffect()
}
