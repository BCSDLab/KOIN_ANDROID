package `in`.koreatech.koin.feature.dining.ui.diningdetail

import `in`.koreatech.koin.domain.model.dining.DiningPlace

sealed interface DiningSideEffect {
    data class FetchDining(val forceRefresh: Boolean) : DiningSideEffect
    data class DiningSoldOutReportSuccess(val place: DiningPlace) : DiningSideEffect
    data object DiningSoldOutNoDiningToReport : DiningSideEffect
    data object DiningSoldOutReportFailed : DiningSideEffect
    data object DiningSoldOutUploadFailed : DiningSideEffect
    data object DiningSoldOutNotOperationTime : DiningSideEffect
    data object DiningSoldOutReportDateNotAllowed : DiningSideEffect
    data object DiningSoldOutReportInvalidImage : DiningSideEffect
    data object DiningSoldOutReportNotFoundDining : DiningSideEffect
    data object DiningSoldOutReportAlreadySoldOut : DiningSideEffect
    data object DiningSoldOutReportAlreadySubmitted : DiningSideEffect
}
