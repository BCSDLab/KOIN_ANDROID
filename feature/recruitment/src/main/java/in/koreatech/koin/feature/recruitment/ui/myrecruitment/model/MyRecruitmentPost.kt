package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

data class MyRecruitmentPost(
    val id: Long,
    val category: RecruitmentCategory,
    val status: RecruitmentStatus,
    val title: String,
    val roles: List<RecruitmentRole> = emptyList(),
    val location: String,
    val dateRange: String,
    val currentApplicants: Int,
    val maxApplicants: Int
)
