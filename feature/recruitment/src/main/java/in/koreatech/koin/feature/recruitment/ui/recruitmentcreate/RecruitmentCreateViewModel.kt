package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentRole
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
class RecruitmentCreateViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<RecruitmentCreateState, RecruitmentCreateSideEffect> {

    override val container = container<RecruitmentCreateState, RecruitmentCreateSideEffect>(
        RecruitmentCreateState()
    )

    fun setCategory(category: TeamRecruitmentCategory): Job = intent {
        reduce { state.copy(category = category, isCategoryDropdownExpanded = false) }
    }

    fun setCategoryDropdownExpanded(expanded: Boolean): Job = intent {
        reduce { state.copy(isCategoryDropdownExpanded = expanded) }
    }

    fun setTitle(title: String): Job = intent {
        reduce { state.copy(title = title) }
    }

    fun setProgressType(progressType: RecruitmentProgressType): Job = intent {
        reduce { state.copy(progressType = progressType) }
    }

    fun showDatePickerDialog(target: DateSelectionTarget): Job = intent {
        reduce {
            state.copy(
                showDatePickerDialog = true,
                dateSelectionTarget = target
            )
        }
    }

    fun dismissDatePickerDialog(): Job = intent {
        reduce { state.copy(showDatePickerDialog = false) }
    }

    fun setRecruitStartDate(date: StableLocalDate): Job = intent {
        reduce { state.copy(recruitStartDate = date, showDatePickerDialog = false) }
    }

    fun setRecruitEndDate(date: StableLocalDate): Job = intent {
        reduce { state.copy(recruitEndDate = date, showDatePickerDialog = false) }
    }

    fun setApplicationDeadline(date: StableLocalDate): Job = intent {
        reduce { state.copy(applicationDeadline = date, showDatePickerDialog = false) }
    }

    fun setRoleCountUndetermined(undetermined: Boolean): Job = intent {
        reduce { state.copy(isRoleCountUndetermined = undetermined) }
    }

    fun addRole(): Job = intent {
        reduce {
            if (state.roles.size >= TeamRecruitmentRole.MAX_ROLE_COUNT) {
                state
            } else {
                state.copy(roles = (state.roles + TeamRecruitmentRole()).toPersistentList())
            }
        }
    }

    fun setRoleName(id: String, name: String): Job = intent {
        reduce {
            state.copy(
                roles = state.roles
                    .map { if (it.id == id) it.copy(name = name) else it }
                    .toPersistentList()
            )
        }
    }

    fun setRoleCount(id: String, count: Int): Job = intent {
        reduce {
            state.copy(
                roles = state.roles
                    .map { if (it.id == id) it.copy(count = count) else it }
                    .toPersistentList()
            )
        }
    }

    fun removeRole(id: String): Job = intent {
        reduce {
            state.copy(roles = state.roles.filterNot { it.id == id }.toPersistentList())
        }
    }

    fun setDescription(description: String): Job = intent {
        reduce { state.copy(description = description) }
    }

    fun setRelatedUrl(url: String): Job = intent {
        reduce { state.copy(relatedUrl = url) }
    }

    fun setQualification(qualification: String): Job = intent {
        reduce { state.copy(qualification = qualification) }
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
        postSideEffect(RecruitmentCreateSideEffect.NavigateUp)
    }

    fun createRecruitment(): Job = intent {
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false) }
        postSideEffect(RecruitmentCreateSideEffect.RecruitmentCreateSuccess)
    }
}
