package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppliedRecruitmentPost(
    val id: Long,
    val category: RecruitmentCategory,
    val applicationStatus: AppliedRecruitmentStatus,
    val daysLeft: Int?,
    val title: String,
    val roles: ImmutableList<RecruitmentRole> = persistentListOf(),
    val location: String,
    val dateRange: String,
    val currentApplicants: Int,
    val maxApplicants: Int
)
