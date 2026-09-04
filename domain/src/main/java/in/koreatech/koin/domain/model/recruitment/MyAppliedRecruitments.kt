package `in`.koreatech.koin.domain.model.recruitment

data class MyAppliedRecruitments(
    val applications: List<MyAppliedRecruitment>,
    val totalCount: Long,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)
