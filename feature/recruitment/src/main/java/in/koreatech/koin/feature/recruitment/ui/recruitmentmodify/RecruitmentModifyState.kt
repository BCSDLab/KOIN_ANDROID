package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.model.RecruitmentModifyRole
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class DateSelectionTarget {
    RECRUIT_START,
    RECRUIT_END,
    DEADLINE
}

@Immutable
data class RecruitmentModifyState(
    val postId: Int = -1,
    val category: RecruitmentCategory = RecruitmentCategory.CONTEST,
    val title: String = "",
    val progressType: RecruitmentProgressType? = null,
    val recruitStartDate: StableLocalDate = StableLocalDate.now(),
    val recruitEndDate: StableLocalDate = StableLocalDate.now(),
    val applicationDeadline: StableLocalDate = StableLocalDate.now(),
    val roles: ImmutableList<RecruitmentModifyRole> = persistentListOf(),
    val isRoleCountUndetermined: Boolean = false,
    val participantCount: Int = RecruitmentModifyRole.MIN_MEMBER_COUNT,
    val description: String = "",
    val relatedUrl: String = "",
    val qualification: String = "",
    val isCategoryDropdownExpanded: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val dateSelectionTarget: DateSelectionTarget = DateSelectionTarget.RECRUIT_START,
    val showSubmitConfirmDialog: Boolean = false,
    val showCancelConfirmDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val isLoading: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = title.isNotBlank() &&
            progressType != null &&
            (roles.isNotEmpty() || isRoleCountUndetermined) &&
            description.isNotBlank()
}
