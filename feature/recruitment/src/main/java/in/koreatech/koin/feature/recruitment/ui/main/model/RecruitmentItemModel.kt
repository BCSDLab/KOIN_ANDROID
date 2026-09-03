package `in`.koreatech.koin.feature.recruitment.ui.main.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.recruitment.Recruitment
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRoleModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentDisplayDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class RecruitmentItemModel(
    val id: Int,
    val category: RecruitmentCategory,
    val status: RecruitmentStatus,
    val dDay: Int?,
    val title: String,
    val roles: ImmutableList<RecruitmentRoleModel> = persistentListOf(),
    val location: RecruitmentLocation,
    val activityStartDate: String,
    val activityEndDate: String,
    val currentCount: Int,
    val maxCount: Int
) {
    val isFull: Boolean get() = currentCount >= maxCount
}

fun Recruitment.toRecruitmentItemModel() = RecruitmentItemModel(
    id = id,
    category = RecruitmentCategory.from(category),
    status = RecruitmentStatus.from(status),
    dDay = dDay,
    title = title,
    roles = roles.map {
        RecruitmentRoleModel(
            id = it.id,
            name = it.name,
            currentParticipants = it.currentParticipants,
            maxParticipants = it.maxParticipants,
            isClosed = it.isClosed
        )
    }.toImmutableList(),
    location = RecruitmentLocation.from(meetingType),
    activityStartDate = activityStartDate.toRecruitmentDisplayDate(),
    activityEndDate = activityEndDate.toRecruitmentDisplayDate(),
    currentCount = currentParticipants,
    maxCount = maxParticipants
)
