package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole

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
