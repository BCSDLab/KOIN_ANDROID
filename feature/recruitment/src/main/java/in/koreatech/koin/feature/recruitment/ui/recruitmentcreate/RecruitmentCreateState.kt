package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import java.time.LocalDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class DateSelectionTarget {
    RECRUIT_START,
    RECRUIT_END,
    DEADLINE
}

@Immutable
data class RecruitmentCreateState(
    val category: TeamRecruitmentCategory = TeamRecruitmentCategory.CONTEST,
    val title: String = "",
    val progressType: RecruitmentProgressType? = null,
    val recruitStartDate: LocalDate = LocalDate.now(),
    val recruitEndDate: LocalDate = LocalDate.now().plusDays(DEFAULT_RECRUIT_PERIOD_DAYS),
    val applicationDeadline: LocalDate = LocalDate.now().plusDays(DEFAULT_RECRUIT_PERIOD_DAYS),
    val roles: ImmutableList<TeamRecruitmentRole> = persistentListOf(),
    val isRoleCountUndetermined: Boolean = false,
    val description: String = "",
    val relatedUrl: String = "",
    val qualification: String = "",
    val isCategoryDropdownExpanded: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val dateSelectionTarget: DateSelectionTarget = DateSelectionTarget.RECRUIT_START,
    val showSubmitConfirmDialog: Boolean = false,
    val showCancelConfirmDialog: Boolean = false,
    val isSubmitting: Boolean = false
) {
    val isSubmitEnabled: Boolean
        get() = title.isNotBlank() &&
            progressType != null &&
            (roles.isNotEmpty() || isRoleCountUndetermined) &&
            description.isNotBlank()
}

private const val DEFAULT_RECRUIT_PERIOD_DAYS = 14L
