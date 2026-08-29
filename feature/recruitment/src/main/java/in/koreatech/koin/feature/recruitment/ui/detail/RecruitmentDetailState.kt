package `in`.koreatech.koin.feature.recruitment.ui.detail

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.detail.model.RecruitmentRoleModel
import `in`.koreatech.koin.feature.recruitment.ui.detail.model.RecruitmentType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentDetailState(
    val id: Int = 0,
    val category: RecruitmentCategory = RecruitmentCategory.ETC,
    val title: String = "",
    val location: RecruitmentLocation = RecruitmentLocation.ONLINE,
    val activityStartDate: String = "",
    val activityEndDate: String = "",
    val dDay: Int = 0,
    val status: RecruitmentStatus = RecruitmentStatus.RECRUITING,
    val recruitmentType: RecruitmentType = RecruitmentType.GENERAL,
    val currentParticipants: Int = 0,
    val maxParticipants: Int = 0,
    val roles: ImmutableList<RecruitmentRoleModel> = persistentListOf(),
    val authorNickname: String = "",
    val description: String = "",
    val qualification: String = "",
    val preference: String = "",
    val createdAt: String = "",
    val isAuthor: Boolean = false,
    val isMoreMenuVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false
) {
    val isClosed: Boolean get() = status == RecruitmentStatus.COMPLETED
}
