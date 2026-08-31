package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

enum class AppliedFilterStatus {
    ALL,
    APPROVED,
    PENDING,
    REJECTED
}

enum class AppliedFilterSort {
    LATEST,
    DEADLINE
}

data class AppliedFilterState(
    val status: AppliedFilterStatus = AppliedFilterStatus.ALL,
    val sort: AppliedFilterSort = AppliedFilterSort.LATEST
)
