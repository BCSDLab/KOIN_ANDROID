package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

enum class RecruitmentFilterStatus {
    ALL,
    RECRUITING,
    COMPLETE
}

enum class RecruitmentFilterSort {
    LATEST,
    DEADLINE
}

data class RecruitmentFilterState(
    val status: RecruitmentFilterStatus = RecruitmentFilterStatus.ALL,
    val sort: RecruitmentFilterSort = RecruitmentFilterSort.LATEST
)
