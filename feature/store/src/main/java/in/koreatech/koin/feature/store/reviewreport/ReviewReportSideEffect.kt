package `in`.koreatech.koin.feature.store.reviewreport

sealed class ReviewReportSideEffect {
    data object ReviewReportSuccess : ReviewReportSideEffect()
}
