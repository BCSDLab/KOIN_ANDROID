package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
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

    fun loadMemberInfo(): Job = intent {
        reduce {
            state.copy(
                isMemberInfoLoaded = true,
                nickname = state.nickname.ifEmpty { "코인유저" },
                department = "컴퓨터공학부",
                studentId = state.studentId.ifEmpty { "2023120203219" }
            )
        }
    }

    fun setNickname(nickname: String): Job = intent {
        if (nickname.length <= NICKNAME_MAX_LENGTH) {
            reduce { state.copy(nickname = nickname) }
        }
    }

    fun setAge(age: String): Job = intent {
        if (age.isEmpty() || (age.all { it.isDigit() } && age.toIntOrNull() in MIN_AGE..MAX_AGE)) {
            reduce { state.copy(age = age) }
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

    fun addSkill(): Job = intent {
        reduce { state.copy(skills = (state.skills + "").toPersistentList()) }
    }

    fun setSkillText(index: Int, text: String): Job = intent {
        reduce {
            state.copy(
                skills = state.skills.mapIndexed { i, skill -> if (i == index) text else skill }.toPersistentList()
            )
        }
    }

    fun removeSkill(index: Int): Job = intent {
        reduce {
            state.copy(
                skills = state.skills.filterIndexed { i, _ -> i != index }.toPersistentList()
            )
        }
    }

    fun showActivityAddForm(): Job = intent {
        reduce { state.copy(activityFormState = ActivityFormState.Adding) }
    }

    fun showActivityEditForm(activity: RecruitmentActivityEntry): Job = intent {
        reduce { state.copy(activityFormState = ActivityFormState.Editing(activity.id)) }
    }

    fun hideActivityForm(): Job = intent {
        reduce { state.copy(activityFormState = ActivityFormState.Hidden) }
    }

    fun addActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce {
            state.copy(
                activities = (state.activities + activity).toPersistentList(),
                activityFormState = ActivityFormState.Hidden
            )
        }
    }

    fun editActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce {
            state.copy(
                activities = state.activities
                    .map { if (it.id == activity.id) activity else it }
                    .toPersistentList(),
                activityFormState = ActivityFormState.Hidden
            )
        }
    }

    fun removeActivity(activity: RecruitmentActivityEntry): Job = intent {
        reduce { state.copy(activities = state.activities.minus(activity).toPersistentList()) }
    }

    fun setSelfIntroduction(text: String): Job = intent {
        reduce { state.copy(selfIntroduction = text) }
    }

    fun goToNextStep(): Job = intent {
        reduce { state.copy(currentStep = RECRUITMENT_APPLY_STEP_COUNT) }
    }

    fun goToPreviousStep(): Job = intent {
        reduce { state.copy(currentStep = 1) }
    }

    fun selectRole(role: TeamRecruitmentRole): Job = intent {
        if (!role.isClosed) {
            reduce { state.copy(selectedRole = role) }
        }
    }

    fun setMotivation(text: String): Job = intent {
        reduce { state.copy(motivation = text) }
    }

    fun setAvailableTime(text: String): Job = intent {
        reduce { state.copy(availableTime = text) }
    }

    fun showSubmitConfirmDialog(): Job = intent {
        reduce { state.copy(showSubmitConfirmDialog = true) }
    }

    fun dismissSubmitConfirmDialog(): Job = intent {
        reduce { state.copy(showSubmitConfirmDialog = false) }
    }

    fun showCancelConfirmDialog(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = true) }
    }

    fun dismissCancelConfirmDialog(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
    }

    fun confirmCancel(): Job = intent {
        reduce { state.copy(showCancelConfirmDialog = false) }
        postSideEffect(RecruitmentApplySideEffect.NavigateUp)
    }

    fun submitApplication(): Job = intent {
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false) }
        postSideEffect(RecruitmentApplySideEffect.ApplySuccess)
    }
}
