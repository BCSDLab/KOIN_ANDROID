package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

enum class RecruitmentFilterStatus {
    ALL,
    RECRUITING,
    COMPLETE
}

fun RecruitmentFilterStatus.toApiValue() = when (this) {
    RecruitmentFilterStatus.ALL -> "ALL"
    RecruitmentFilterStatus.RECRUITING -> "RECRUITING"
    RecruitmentFilterStatus.COMPLETE -> "CLOSED"
}

enum class RecruitmentFilterSort {
    LATEST,
    DEADLINE
}

fun RecruitmentFilterSort.toApiValue() = when (this) {
    RecruitmentFilterSort.LATEST -> "LATEST_DESC"
    RecruitmentFilterSort.DEADLINE -> "DEADLINE_ASC"
}

data class RecruitmentFilterState(
    val status: RecruitmentFilterStatus = RecruitmentFilterStatus.ALL,
    val sort: RecruitmentFilterSort = RecruitmentFilterSort.LATEST
)
