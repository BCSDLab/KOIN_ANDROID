package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model

import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost as DomainMyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.model.toDateRange
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentRole
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
    val maxApplicants: Int,
    val teamChatRoomId: Int? = null
)

fun DomainMyRecruitmentPost.toMyRecruitmentPost() = MyRecruitmentPost(
    id = id,
    category = category.toRecruitmentCategory(),
    status = when (status) {
        "RECRUITING" -> RecruitmentStatus.Recruiting(dDay)
        else -> RecruitmentStatus.Complete
    },
    title = title,
    roles = roles.map { it.toRecruitmentRole() }.toPersistentList(),
    location = meetingType.toRecruitmentLocation(),
    dateRange = activityStartDate.toDateRange(activityEndDate),
    currentApplicants = currentParticipants,
    maxApplicants = maxParticipants,
    teamChatRoomId = teamChatRoomId
)
