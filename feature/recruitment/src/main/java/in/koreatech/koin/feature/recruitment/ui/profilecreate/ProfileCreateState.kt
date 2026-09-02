package `in`.koreatech.koin.feature.recruitment.ui.profilecreate

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

const val PROFILE_CREATE_STEP_COUNT = 2
const val PROFILE_NICKNAME_MAX_LENGTH = 20
const val PROFILE_PREFERRED_ROLE_MAX_LENGTH = 20

sealed interface ProfileActivityFormState {
    data object Hidden : ProfileActivityFormState
    data object Adding : ProfileActivityFormState
    data class Editing(val activityId: Long) : ProfileActivityFormState
}

@Immutable
data class ProfileCreateState(
    val isEditMode: Boolean = false,
    val currentStep: Int = 1,
    val nickname: String = "",
    val department: String = "",
    val isDepartmentDropdownExpanded: Boolean = false,
    val studentId: String = "",
    val preferredRole: String = "",
    val skills: ImmutableList<String> = persistentListOf(),
    val activities: ImmutableList<RecruitmentActivityEntry> = persistentListOf(),
    val activityFormState: ProfileActivityFormState = ProfileActivityFormState.Hidden,
    val selfIntroduction: String = "",
    val showSaveConfirmDialog: Boolean = false,
    val showCancelConfirmDialog: Boolean = false,
    val isSaving: Boolean = false
) {
    val isStepOneValid: Boolean
        get() = nickname.isNotBlank() && department.isNotBlank() && studentId.isNotBlank()

    val isSaveEnabled: Boolean
        get() = preferredRole.isNotBlank() && selfIntroduction.isNotBlank()
}
