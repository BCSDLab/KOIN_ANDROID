package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.SkillEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRoleOption
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

const val RECRUITMENT_APPLY_STEP_COUNT = 2
const val NICKNAME_MAX_LENGTH = 20
const val MOTIVATION_MAX_LENGTH = 1000
const val AVAILABLE_TIME_MAX_LENGTH = 100
const val SELF_INTRODUCTION_MAX_LENGTH = 1000

sealed interface ActivityFormState {
    data object Hidden : ActivityFormState
    data object Adding : ActivityFormState
    data class Editing(val activityId: Long) : ActivityFormState
}

@Immutable
data class RecruitmentApplyState(
    val recruitmentId: Int = 0,
    val currentStep: Int = 1,
    val isMemberInfoLoaded: Boolean = false,
    val nickname: String = "",
    val department: String = "",
    val departments: ImmutableList<String> = persistentListOf(),
    val isDepartmentDropdownExpanded: Boolean = false,
    val studentId: String = "",
    val skills: ImmutableList<SkillEntry> = persistentListOf(),
    val activities: ImmutableList<RecruitmentActivityEntry> = persistentListOf(),
    val activityFormState: ActivityFormState = ActivityFormState.Hidden,
    val selfIntroduction: String = "",
    val availableRoles: ImmutableList<TeamRecruitmentRoleOption> = persistentListOf(),
    val selectedRole: TeamRecruitmentRoleOption? = null,
    val motivation: String = "",
    val availableTime: String = "",
    val showSubmitConfirmDialog: Boolean = false,
    val showCancelConfirmDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
) {
    val isStepOneValid: Boolean
        get() = nickname.isNotBlank() &&
            department.isNotBlank() &&
            studentId.isNotBlank() &&
            selfIntroduction.isNotBlank()

    val isSubmitEnabled: Boolean
        get() = selectedRole != null && motivation.isNotBlank() && availableTime.isNotBlank()
}
