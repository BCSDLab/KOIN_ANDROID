package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

const val RECRUITMENT_APPLY_STEP_COUNT = 2
const val NICKNAME_MAX_LENGTH = 20

sealed interface ActivityFormState {
    data object Hidden : ActivityFormState
    data object Adding : ActivityFormState
    data class Editing(val activityId: Long) : ActivityFormState
}

@Immutable
data class RecruitmentApplyState(
    val currentStep: Int = 1,
    val isMemberInfoLoaded: Boolean = false,
    val nickname: String = "",
    val age: String = "",
    val department: String = "",
    val isDepartmentDropdownExpanded: Boolean = false,
    val studentId: String = "",
    val skills: ImmutableList<String> = persistentListOf(),
    val activities: ImmutableList<RecruitmentActivityEntry> = persistentListOf(),
    val activityFormState: ActivityFormState = ActivityFormState.Hidden,
    val selfIntroduction: String = "",
    val availableRoles: ImmutableList<TeamRecruitmentRole> = persistentListOf(),
    val selectedRole: TeamRecruitmentRole? = null,
    val motivation: String = "",
    val availableTime: String = "",
    val showSubmitConfirmDialog: Boolean = false,
    val showCancelConfirmDialog: Boolean = false,
    val isSubmitting: Boolean = false
) {
    val isStepOneValid: Boolean
        get() = nickname.isNotBlank() &&
                age.isNotBlank() &&
                department.isNotBlank() &&
                studentId.isNotBlank() &&
                selfIntroduction.isNotBlank()

    val isSubmitEnabled: Boolean
        get() = selectedRole != null && motivation.isNotBlank() && availableTime.isNotBlank()
}
