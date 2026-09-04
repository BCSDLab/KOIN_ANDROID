package `in`.koreatech.koin.feature.recruitment.ui.profilecreate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.withNewSkill
import `in`.koreatech.koin.feature.recruitment.model.withSkillText
import `in`.koreatech.koin.feature.recruitment.model.withoutSkill
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
@Suppress("TooManyFunctions")
class ProfileCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ProfileCreateState, ProfileCreateSideEffect> {

    private val arguments = savedStateHandle.toRoute<RecruitmentNavType.ProfileCreate>()

    override val container = container<ProfileCreateState, ProfileCreateSideEffect>(
        ProfileCreateState(isEditMode = arguments.isEditMode)
    )

    fun loadMemberInfo() = intent {
        reduce {
            state.copy(
                nickname = state.nickname.ifEmpty { "코인유저" },
                department = "컴퓨터공학부",
                studentId = state.studentId.ifEmpty { "2023100000" }
            )
        }
    }

    fun setNickname(nickname: String) = intent {
        if (nickname.length <= PROFILE_NICKNAME_MAX_LENGTH) {
            reduce { state.copy(nickname = nickname) }
        }
    }

    fun setDepartmentDropdownExpanded(expanded: Boolean) = intent {
        reduce { state.copy(isDepartmentDropdownExpanded = expanded) }
    }

    fun setDepartment(department: String) = intent {
        reduce { state.copy(department = department, isDepartmentDropdownExpanded = false) }
    }

    fun setStudentId(studentId: String) = intent {
        reduce { state.copy(studentId = studentId) }
    }

    fun setPreferredRole(role: String) = intent {
        if (role.length <= PROFILE_PREFERRED_ROLE_MAX_LENGTH) {
            reduce { state.copy(preferredRole = role) }
        }
    }

    fun addSkill() = intent {
        reduce { state.copy(skills = state.skills.withNewSkill()) }
    }

    fun setSkillText(id: Long, text: String) = intent {
        reduce { state.copy(skills = state.skills.withSkillText(id, text)) }
    }

    fun removeSkill(id: Long) = intent {
        reduce { state.copy(skills = state.skills.withoutSkill(id)) }
    }

    fun showActivityAddForm() = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Adding) }
    }

    fun showActivityEditForm(activity: RecruitmentActivityEntry) = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Editing(activity.id)) }
    }

    fun hideActivityForm() = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Hidden) }
    }

    fun addActivity(activity: RecruitmentActivityEntry) = intent {
        reduce {
            state.copy(
                activities = (state.activities + activity).toPersistentList(),
                activityFormState = ProfileActivityFormState.Hidden
            )
        }
    }

    fun editActivity(activity: RecruitmentActivityEntry) = intent {
        reduce {
            state.copy(
                activities = state.activities
                    .map { if (it.id == activity.id) activity else it }
                    .toPersistentList(),
                activityFormState = ProfileActivityFormState.Hidden
            )
        }
    }

    fun removeActivity(activity: RecruitmentActivityEntry) = intent {
        reduce { state.copy(activities = state.activities.minus(activity).toPersistentList()) }
    }

    fun setSelfIntroduction(text: String) = intent {
        if (text.length <= SELF_INTRODUCTION_MAX_LENGTH) {
            reduce { state.copy(selfIntroduction = text) }
        }
    }

    fun goToNextStep() = intent {
        reduce { state.copy(currentStep = PROFILE_CREATE_STEP_COUNT) }
    }

    fun goToPreviousStep() = intent {
        reduce { state.copy(currentStep = 1) }
    }

    fun showSaveConfirmDialog() = intent {
        reduce { state.copy(showSaveConfirmDialog = true) }
    }

    fun dismissSaveConfirmDialog() = intent {
        reduce { state.copy(showSaveConfirmDialog = false) }
    }

    fun showCancelConfirmDialog() = intent {
        reduce { state.copy(showCancelConfirmDialog = true) }
    }

    fun dismissCancelConfirmDialog() = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
    }

    fun confirmCancel() = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
        postSideEffect(ProfileCreateSideEffect.NavigateUp)
    }

    fun saveProfile() = intent {
        reduce { state.copy(isSaving = true, showSaveConfirmDialog = false) }
        postSideEffect(ProfileCreateSideEffect.SaveSuccess)
    }
}
