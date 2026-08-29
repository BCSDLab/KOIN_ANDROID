package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.model.toDateRange
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class AppliedRecruitmentPost(
    val id: Int,
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

fun MyAppliedRecruitment.toAppliedRecruitmentPost() = AppliedRecruitmentPost(
    id = applicationId,
    category = recruitment.category.toRecruitmentCategory(),
    applicationStatus = when (status) {
        "ACCEPTED" -> AppliedRecruitmentStatus.Approved
        "PENDING" -> AppliedRecruitmentStatus.Pending
        else -> AppliedRecruitmentStatus.Rejected
    },
    daysLeft = if (recruitment.status == "RECRUITING") recruitment.dDay else null,
    title = recruitment.title,
    roles = recruitment.roles.map { it.toRecruitmentRole() }.toPersistentList(),
    location = recruitment.meetingType.toRecruitmentLocation(),
    dateRange = recruitment.activityStartDate.toDateRange(recruitment.activityEndDate),
    currentApplicants = recruitment.currentParticipants,
    maxApplicants = recruitment.maxParticipants
)
