package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.TeamRecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentCategory
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class RecruitmentCreateViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<RecruitmentCreateState, RecruitmentCreateSideEffect> {

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

    fun setRecruitStartDate(date: LocalDate) = intent {
        reduce { state.copy(recruitStartDate = date, showDatePickerDialog = false) }
    }

    fun setRecruitEndDate(date: LocalDate) = intent {
        reduce { state.copy(recruitEndDate = date, showDatePickerDialog = false) }
    }

    fun setApplicationDeadline(date: LocalDate) = intent {
        reduce { state.copy(applicationDeadline = date, showDatePickerDialog = false) }
    }

    fun setRoleCountUndetermined(undetermined: Boolean) = intent {
        reduce { state.copy(isRoleCountUndetermined = undetermined) }
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
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false) }
        postSideEffect(RecruitmentCreateSideEffect.RecruitmentCreateSuccess)
    }
}
