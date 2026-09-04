package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dept.GetDeptNamesUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.ApplyTeamRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.recruitment.mapper.toRecruitmentErrorMessage
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRoleOption
import `in`.koreatech.koin.feature.recruitment.model.withNewSkill
import `in`.koreatech.koin.feature.recruitment.model.withSkillText
import `in`.koreatech.koin.feature.recruitment.model.withoutSkill
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentRoleArg
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentRoleArgListNavType
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private const val MIN_AGE = 1
private const val MAX_AGE = 99

@Suppress("TooManyFunctions")
@HiltViewModel
class RecruitmentApplyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val applyTeamRecruitmentUseCase: ApplyTeamRecruitmentUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getDeptNamesUseCase: GetDeptNamesUseCase
) : ViewModel(), ContainerHost<RecruitmentApplyState, RecruitmentApplySideEffect> {

    private val arguments = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentApply>(
        typeMap = mapOf(
            typeOf<List<RecruitmentRoleArg>>() to RecruitmentRoleArgListNavType
        )
    )

    override val container = container<RecruitmentApplyState, RecruitmentApplySideEffect>(
        RecruitmentApplyState(
            recruitmentId = arguments.recruitmentId,
            availableRoles = arguments.roles
                .map { TeamRecruitmentRoleOption(id = it.id, name = it.name, isClosed = it.isClosed) }
                .toPersistentList()
        )
    ) {
        loadDepartments()
    }

    private fun loadDepartments() = intent {
        val departments = getDeptNamesUseCase()
        reduce { state.copy(departments = departments.toPersistentList()) }
    }

    fun loadMemberInfo() = intent {
        getUserInfoUseCase()
            .onSuccess { user ->
                if (user is User.Student) {
                    reduce {
                        state.copy(
                            isMemberInfoLoaded = true,
                            nickname = user.nickname ?: state.nickname,
                            department = user.major ?: state.department,
                            studentId = user.studentNumber ?: state.studentId
                        )
                    }
                }
            }
            .onFailure { throwable ->
                reduce { state.copy(errorMessage = throwable.toRecruitmentErrorMessage()) }
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
        if (text.length <= SELF_INTRODUCTION_MAX_LENGTH) {
            reduce { state.copy(selfIntroduction = text) }
        }
    }

    fun goToNextStep() = intent {
        reduce { state.copy(currentStep = RECRUITMENT_APPLY_STEP_COUNT) }
    }

    fun goToPreviousStep() = intent {
        reduce { state.copy(currentStep = 1) }
    }

    fun selectRole(role: TeamRecruitmentRoleOption) = intent {
        if (!role.isClosed) {
            reduce { state.copy(selectedRole = role) }
        }
    }

    fun setMotivation(text: String) = intent {
        if (text.length <= MOTIVATION_MAX_LENGTH) {
            reduce { state.copy(motivation = text) }
        }
    }

    fun setAvailableTime(text: String) = intent {
        if (text.length <= AVAILABLE_TIME_MAX_LENGTH) {
            reduce { state.copy(availableTime = text) }
        }
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
        val role = state.selectedRole ?: return@intent
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false, errorMessage = null) }

        applyTeamRecruitmentUseCase(
            recruitmentId = state.recruitmentId,
            roleId = role.id,
            motivation = state.motivation,
            availability = state.availableTime
        ).onSuccess {
            reduce { state.copy(isSubmitting = false) }
            postSideEffect(RecruitmentApplySideEffect.ApplySuccess)
        }.onFailure { throwable ->
            reduce { state.copy(isSubmitting = false, errorMessage = throwable.toRecruitmentErrorMessage()) }
            postSideEffect(RecruitmentApplySideEffect.ApplyFailure)
        }
    }
}
