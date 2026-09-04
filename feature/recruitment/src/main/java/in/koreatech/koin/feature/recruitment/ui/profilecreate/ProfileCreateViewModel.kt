package `in`.koreatech.koin.feature.recruitment.ui.profilecreate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.SkillEntry
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
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

    fun loadMemberInfo(): Job = intent {
        reduce {
            state.copy(
                nickname = state.nickname.ifEmpty { "코인유저" },
                department = "컴퓨터공학부",
                studentId = state.studentId.ifEmpty { "2023100000" }
            )
        }
    }

    fun setNickname(nickname: String): Job = intent {
        if (nickname.length <= PROFILE_NICKNAME_MAX_LENGTH) {
            reduce { state.copy(nickname = nickname) }
        }
    }

    fun setDepartmentDropdownExpanded(expanded: Boolean): Job = intent {
        reduce { state.copy(isDepartmentDropdownExpanded = expanded) }
    }

    fun setDepartment(department: String): Job = intent {
        reduce { state.copy(department = department, isDepartmentDropdownExpanded = false) }
    }

    fun setStudentId(studentId: String): Job = intent {
        reduce { state.copy(studentId = studentId) }
    }

    fun setPreferredRole(role: String): Job = intent {
        if (role.length <= PROFILE_PREFERRED_ROLE_MAX_LENGTH) {
            reduce { state.copy(preferredRole = role) }
        }
    }

    fun addSkill(): Job = intent {
        reduce {
            val nextId = (state.skills.maxOfOrNull { it.id } ?: 0L) + 1L
            state.copy(skills = (state.skills + SkillEntry(id = nextId, text = "")).toPersistentList())
        }
    }

    fun setSkillText(id: Long, text: String): Job = intent {
        reduce {
            state.copy(
                skills = state.skills.map { skill ->
                    if (skill.id == id) skill.copy(text = text) else skill
                }.toPersistentList()
            )
        }
    }

    fun removeSkill(id: Long): Job = intent {
        reduce {
            state.copy(skills = state.skills.filterNot { it.id == id }.toPersistentList())
        }
    }

    fun showActivityAddForm(): Job = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Adding) }
    }

    fun showActivityEditForm(activity: RecruitmentActivityEntry): Job = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Editing(activity.id)) }
    }

    fun hideActivityForm(): Job = intent {
        reduce { state.copy(activityFormState = ProfileActivityFormState.Hidden) }
    }

    fun addActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce {
            state.copy(
                activities = (state.activities + activity).toPersistentList(),
                activityFormState = ProfileActivityFormState.Hidden
            )
        }
    }

    fun editActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce {
            state.copy(
                activities = state.activities
                    .map { if (it.id == activity.id) activity else it }
                    .toPersistentList(),
                activityFormState = ProfileActivityFormState.Hidden
            )
        }
    }

    fun removeActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce { state.copy(activities = state.activities.minus(activity).toPersistentList()) }
    }

    fun setSelfIntroduction(text: String): Job = intent {
        if (text.length <= SELF_INTRODUCTION_MAX_LENGTH) {
            reduce { state.copy(selfIntroduction = text) }
        }
    }

    fun goToNextStep(): Job = intent {
        reduce { state.copy(currentStep = PROFILE_CREATE_STEP_COUNT) }
    }

    fun goToPreviousStep(): Job = intent {
        reduce { state.copy(currentStep = 1) }
    }

    fun showSaveConfirmDialog(): Job = intent {
        reduce { state.copy(showSaveConfirmDialog = true) }
    }

    fun dismissSaveConfirmDialog(): Job = intent {
        reduce { state.copy(showSaveConfirmDialog = false) }
    }

    fun showCancelConfirmDialog(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = true) }
    }

    fun dismissCancelConfirmDialog(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
    }

    fun confirmCancel(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
        postSideEffect(ProfileCreateSideEffect.NavigateUp)
    }

    fun saveProfile(): Job = intent {
        reduce { state.copy(isSaving = true, showSaveConfirmDialog = false) }
        postSideEffect(ProfileCreateSideEffect.SaveSuccess)
    }
}
