package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MyRecruitmentPost(
    val id: Long,
    val category: RecruitmentCategory,
    val status: RecruitmentStatus,
    val title: String,
    val roles: ImmutableList<RecruitmentRole> = persistentListOf(),
    val location: String,
    val dateRange: String,
    val currentApplicants: Int,
    val maxApplicants: Int
)
