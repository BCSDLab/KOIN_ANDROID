package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput
import `in`.koreatech.koin.domain.usecase.recruitment.CreateTeamRecruitmentUseCase
import `in`.koreatech.koin.feature.recruitment.mapper.toRecruitmentErrorMessage
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentCategory
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private val ISO_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

@HiltViewModel
class RecruitmentCreateViewModel @Inject constructor(
    private val createTeamRecruitmentUseCase: CreateTeamRecruitmentUseCase
) : ViewModel(), ContainerHost<RecruitmentCreateState, RecruitmentCreateSideEffect> {

    override val container = container<RecruitmentCreateState, RecruitmentCreateSideEffect>(
        RecruitmentCreateState()
    )

    fun setCategory(category: TeamRecruitmentCategory) = intent {
        reduce { state.copy(category = category, isCategoryDropdownExpanded = false) }
    }

    fun setCategoryDropdownExpanded(expanded: Boolean) = intent {
        reduce { state.copy(isCategoryDropdownExpanded = expanded) }
    }

    fun setTitle(title: String) = intent {
        reduce { state.copy(title = title) }
    }

    fun setProgressType(progressType: RecruitmentProgressType) = intent {
        reduce { state.copy(progressType = progressType) }
    }

    fun showDatePickerDialog(target: DateSelectionTarget) = intent {
        reduce {
            state.copy(
                showDatePickerDialog = true,
                dateSelectionTarget = target
            )
        }
    }

    fun dismissDatePickerDialog() = intent {
        reduce { state.copy(showDatePickerDialog = false) }
    }

    fun setRecruitStartDate(date: StableLocalDate) = intent {
        reduce { state.copy(recruitStartDate = date, showDatePickerDialog = false) }
    }

    fun setRecruitEndDate(date: StableLocalDate) = intent {
        reduce { state.copy(recruitEndDate = date, showDatePickerDialog = false) }
    }

    fun setApplicationDeadline(date: StableLocalDate) = intent {
        reduce { state.copy(applicationDeadline = date, showDatePickerDialog = false) }
    }

    fun setRoleCountUndetermined(undetermined: Boolean) = intent {
        reduce {
            state.copy(
                isRoleCountUndetermined = undetermined,
                // GENERAL(역할 구분 없음)과 ROLE_BASED(역할별 모집)는 동시에 값을 보낼 수 없으므로
                // 모드를 전환하면 역할 목록을 비웁니다.
                roles = if (undetermined) persistentListOf() else state.roles
            )
        }
    }

    fun setMaxParticipants(count: Int) = intent {
        if (count in MIN_TOTAL_PARTICIPANTS..MAX_TOTAL_PARTICIPANTS) {
            reduce { state.copy(maxParticipants = count) }
        }
    }

    fun addRole() = intent {
        reduce {
            if (state.roles.size >= TeamRecruitmentRole.MAX_ROLE_COUNT) {
                state
            } else {
                state.copy(roles = (state.roles + TeamRecruitmentRole()).toPersistentList())
            }
        }
    }

    fun setRoleName(id: String, name: String) = intent {
        reduce {
            state.copy(
                roles = state.roles
                    .map { if (it.id == id) it.copy(name = name) else it }
                    .toPersistentList()
            )
        }
    }

    fun setRoleCount(id: String, count: Int) = intent {
        reduce {
            state.copy(
                roles = state.roles
                    .map { if (it.id == id) it.copy(count = count) else it }
                    .toPersistentList()
            )
        }
    }

    fun removeRole(id: String) = intent {
        reduce {
            state.copy(roles = state.roles.filterNot { it.id == id }.toPersistentList())
        }
    }

    fun setDescription(description: String) = intent {
        reduce { state.copy(description = description) }
    }

    fun setRelatedUrl(url: String) = intent {
        reduce { state.copy(relatedUrl = url) }
    }

    fun setQualification(qualification: String) = intent {
        reduce { state.copy(qualification = qualification) }
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
        postSideEffect(RecruitmentCreateSideEffect.NavigateUp)
    }

    fun createRecruitment() = intent {
        val progressType = state.progressType ?: return@intent
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false, errorMessage = null) }

        createTeamRecruitmentUseCase(
            category = state.category.name,
            title = state.title,
            meetingType = progressType.name,
            activityStartDate = state.recruitStartDate.value.format(ISO_DATE_FORMATTER),
            activityEndDate = state.recruitEndDate.value.format(ISO_DATE_FORMATTER),
            deadlineDate = state.applicationDeadline.value.format(ISO_DATE_FORMATTER),
            recruitmentType = if (state.isRoleCountUndetermined) "GENERAL" else "ROLE_BASED",
            maxParticipants = if (state.isRoleCountUndetermined) state.maxParticipants else null,
            roles = if (state.isRoleCountUndetermined) {
                emptyList()
            } else {
                state.roles.map { role -> TeamRecruitmentRoleInput(name = role.name.trim(), maxParticipants = role.count) }
            },
            description = state.description,
            relatedUrl = state.relatedUrl.trim().ifBlank { null },
            qualification = state.qualification.trim().ifBlank { null }
        ).onSuccess {
            reduce { state.copy(isSubmitting = false) }
            postSideEffect(RecruitmentCreateSideEffect.RecruitmentCreateSuccess)
        }.onFailure { throwable ->
            reduce { state.copy(isSubmitting = false, errorMessage = throwable.toRecruitmentErrorMessage()) }
            postSideEffect(RecruitmentCreateSideEffect.RecruitmentCreateFailure)
        }
    }
}
