package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.model.withNewSkill
import `in`.koreatech.koin.feature.recruitment.model.withSkillText
import `in`.koreatech.koin.feature.recruitment.model.withoutSkill
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private const val MIN_AGE = 1
private const val MAX_AGE = 99

@HiltViewModel
@Suppress("TooManyFunctions")
class RecruitmentApplyViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<RecruitmentApplyState, RecruitmentApplySideEffect> {

    override val container = container<RecruitmentApplyState, RecruitmentApplySideEffect>(
        RecruitmentApplyState()
    )

    fun loadMemberInfo() = intent {
        reduce {
            state.copy(
                isMemberInfoLoaded = true,
                nickname = state.nickname.ifEmpty { "코인유저" },
                department = "컴퓨터공학부",
                studentId = state.studentId.ifEmpty { "2023120203219" }
            )
        }
    }

    fun setNickname(nickname: String) = intent {
        if (nickname.length <= NICKNAME_MAX_LENGTH) {
            reduce { state.copy(nickname = nickname) }
        }
    }

    fun setAge(age: String) = intent {
        if (age.isEmpty() || (age.all { it.isDigit() } && age.toIntOrNull() in MIN_AGE..MAX_AGE)) {
            reduce { state.copy(age = age) }
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
        reduce { state.copy(activityFormState = ActivityFormState.Adding) }
    }

    fun showActivityEditForm(activity: RecruitmentActivityEntry) = intent {
        reduce { state.copy(activityFormState = ActivityFormState.Editing(activity.id)) }
    }

    fun hideActivityForm() = intent {
        reduce { state.copy(activityFormState = ActivityFormState.Hidden) }
    }

    fun addActivity(activity: RecruitmentActivityEntry) = intent {
        reduce {
            state.copy(
                activities = (state.activities + activity).toPersistentList(),
                activityFormState = ActivityFormState.Hidden
            )
        }
    }

    fun editActivity(activity: RecruitmentActivityEntry) = intent {
        reduce {
            state.copy(
                activities = state.activities
                    .map { if (it.id == activity.id) activity else it }
                    .toPersistentList(),
                activityFormState = ActivityFormState.Hidden
            )
        }
    }

    fun removeActivity(activity: RecruitmentActivityEntry) = intent {
        reduce { state.copy(activities = state.activities.minus(activity).toPersistentList()) }
    }

    fun setSelfIntroduction(text: String) = intent {
        reduce { state.copy(selfIntroduction = text) }
    }

    fun goToNextStep() = intent {
        reduce { state.copy(currentStep = RECRUITMENT_APPLY_STEP_COUNT) }
    }

    fun goToPreviousStep() = intent {
        reduce { state.copy(currentStep = 1) }
    }

    fun selectRole(role: TeamRecruitmentRole) = intent {
        if (!role.isClosed) {
            reduce { state.copy(selectedRole = role) }
        }
    }

    fun setMotivation(text: String) = intent {
        reduce { state.copy(motivation = text) }
    }

    fun setAvailableTime(text: String) = intent {
        reduce { state.copy(availableTime = text) }
    }

    fun showSubmitConfirmDialog() = intent {
        reduce { state.copy(showSubmitConfirmDialog = true) }
    }

    fun dismissSubmitConfirmDialog() = intent {
        reduce { state.copy(showSubmitConfirmDialog = false) }
    }

    fun showCancelConfirmDialog() = intent {
        reduce { state.copy(showCancelConfirmDialog = true) }
    }

    fun dismissCancelConfirmDialog() = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
    }

    fun confirmCancel() = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
        postSideEffect(RecruitmentApplySideEffect.NavigateUp)
    }

    fun submitApplication() = intent {
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false) }
        postSideEffect(RecruitmentApplySideEffect.ApplySuccess)
    }
}
