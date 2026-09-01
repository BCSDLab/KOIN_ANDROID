package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.model.RecruitmentModifyRole
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@Suppress("TooManyFunctions")
@HiltViewModel
class RecruitmentModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<RecruitmentModifyState, RecruitmentModifySideEffect> {

    private val postId = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentModify>().postId

    // TODO: 모집글 상세 조회 API가 추가되면 postId로 실제 데이터를 불러와 초기 state를 구성한다.
    override val container = container<RecruitmentModifyState, RecruitmentModifySideEffect>(
        RecruitmentModifyState(postId = postId)
    )

    fun setCategory(category: RecruitmentCategory) = intent {
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
        reduce { state.copy(isRoleCountUndetermined = undetermined) }
    }

    fun addRole() = intent {
        reduce {
            if (state.roles.size >= RecruitmentModifyRole.MAX_ROLE_COUNT) {
                state
            } else {
                state.copy(roles = (state.roles + RecruitmentModifyRole()).toPersistentList())
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
        postSideEffect(RecruitmentModifySideEffect.NavigateUp)
    }

    fun modifyRecruitment() = intent {
        reduce { state.copy(isSubmitting = true, showSubmitConfirmDialog = false) }
        postSideEffect(RecruitmentModifySideEffect.RecruitmentModifySuccess)
    }
}
