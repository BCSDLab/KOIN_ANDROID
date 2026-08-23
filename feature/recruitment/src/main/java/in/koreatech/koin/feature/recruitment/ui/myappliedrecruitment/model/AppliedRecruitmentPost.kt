package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentRole

data class AppliedRecruitmentPost(
    val id: Long,
    val category: RecruitmentCategory,
    val applicationStatus: AppliedRecruitmentStatus,
    val daysLeft: Int?,
    val title: String,
    val roles: List<RecruitmentRole> = emptyList(),
    val location: String,
    val dateRange: String,
    val currentApplicants: Int,
    val maxApplicants: Int
)
