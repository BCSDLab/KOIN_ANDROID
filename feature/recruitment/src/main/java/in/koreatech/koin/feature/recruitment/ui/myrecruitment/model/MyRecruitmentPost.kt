package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost as DomainMyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole as DomainRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

data class MyRecruitmentPost(
    val id: Int,
    val category: RecruitmentCategory,
    val status: RecruitmentStatus,
    val title: String,
    val roles: ImmutableList<RecruitmentRole> = persistentListOf(),
    val location: String,
    val dateRange: String,
    val currentApplicants: Int,
    val maxApplicants: Int
)

fun DomainMyRecruitmentPost.toMyRecruitmentPost() = MyRecruitmentPost(
    id = id,
    category = RecruitmentCategory.entries.firstOrNull { it.name == category } ?: RecruitmentCategory.ETC,
    status = when (status) {
        "RECRUITING" -> RecruitmentStatus.Recruiting(dDay)
        else -> RecruitmentStatus.Complete
    },
    title = title,
    roles = roles.map { it.toRecruitmentRole() }.toPersistentList(),
    location = when (meetingType) {
        "ONLINE" -> "온라인"
        "OFFLINE" -> "오프라인"
        else -> "온·오프라인"
    },
    dateRange = "${activityStartDate.replace("-", ".")} ~ ${activityEndDate.replace("-", ".")}",
    currentApplicants = currentParticipants,
    maxApplicants = maxParticipants
)

fun DomainRecruitmentRole.toRecruitmentRole() = RecruitmentRole(
    name = name,
    count = maxParticipants
)
