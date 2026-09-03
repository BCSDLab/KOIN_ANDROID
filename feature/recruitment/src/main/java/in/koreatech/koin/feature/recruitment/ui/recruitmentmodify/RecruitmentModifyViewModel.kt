package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdateRole
import `in`.koreatech.koin.domain.usecase.recruitment.GetRecruitmentDetailUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.UpdateRecruitmentUseCase
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentType
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.model.RecruitmentModifyRole
import `in`.koreatech.koin.feature.recruitment.utils.toApiDateText
import `in`.koreatech.koin.feature.recruitment.utils.toStableLocalDate
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@Suppress("TooManyFunctions")
@HiltViewModel
class RecruitmentModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecruitmentDetailUseCase: GetRecruitmentDetailUseCase,
    private val updateRecruitmentUseCase: UpdateRecruitmentUseCase
) : ViewModel(),
    ContainerHost<RecruitmentModifyState, RecruitmentModifySideEffect> {

    private val postId = savedStateHandle.toRoute<RecruitmentNavType.RecruitmentModify>().postId

    override val container = container<RecruitmentModifyState, RecruitmentModifySideEffect>(
        RecruitmentModifyState(postId = postId)
    ) {
        fetchRecruitmentDetail()
    }

    fun fetchRecruitmentDetail() = intent {
        reduce { state.copy(isLoading = true) }
        getRecruitmentDetailUseCase(recruitmentId = postId)
            .onSuccess { detail ->
                val isRoleBased = RecruitmentType.from(detail.recruitmentType) == RecruitmentType.ROLE_BASED
                reduce {
                    state.copy(
                        category = RecruitmentCategory.from(detail.category),
                        title = detail.title,
                        progressType = RecruitmentProgressType.from(detail.meetingType),
                        recruitStartDate = detail.activityStartDate.toStableLocalDate(),
                        recruitEndDate = detail.activityEndDate.toStableLocalDate(),
                        applicationDeadline = detail.deadlineDate.toStableLocalDate(),
                        roles = if (isRoleBased) {
                            detail.roles.map { role ->
                                RecruitmentModifyRole(
                                    name = role.name,
                                    count = role.maxParticipants,
                                    roleId = role.id
                                )
                            }.toPersistentList()
                        } else {
                            persistentListOf()
                        },
                        isRoleCountUndetermined = !isRoleBased,
                        participantCount = if (isRoleBased) state.participantCount else detail.maxParticipants,
                        description = detail.description,
                        relatedUrl = detail.relatedUrl.orEmpty(),
                        qualification = detail.qualification.orEmpty(),
                        isLoading = false
                    )
                }
            }
            .onFailure { exception ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentModifySideEffect.ShowLoadError(exception.message))
            }
    }

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

    fun setParticipantCount(count: Int) = intent {
        reduce { state.copy(participantCount = count) }
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
        updateRecruitmentUseCase(recruitmentId = postId, update = state.toRecruitmentUpdate())
            .onSuccess {
                reduce { state.copy(isSubmitting = false) }
                postSideEffect(RecruitmentModifySideEffect.RecruitmentModifySuccess)
            }
            .onFailure { exception ->
                reduce { state.copy(isSubmitting = false) }
                postSideEffect(RecruitmentModifySideEffect.RecruitmentModifyFailure(exception.message))
            }
    }

    private fun RecruitmentModifyState.toRecruitmentUpdate(): RecruitmentUpdate {
        val isRoleBased = !isRoleCountUndetermined
        return RecruitmentUpdate(
            category = category.apiValue,
            title = title,
            meetingType = requireNotNull(progressType).apiValue,
            activityStartDate = recruitStartDate.value.toApiDateText(),
            activityEndDate = recruitEndDate.value.toApiDateText(),
            deadlineDate = applicationDeadline.value.toApiDateText(),
            recruitmentType = if (isRoleBased) RecruitmentType.ROLE_BASED.apiValue else RecruitmentType.GENERAL.apiValue,
            maxParticipants = if (isRoleBased) null else participantCount,
            roles = if (isRoleBased) {
                roles.map { role ->
                    RecruitmentUpdateRole(id = role.roleId, name = role.name, maxParticipants = role.count)
                }
            } else {
                emptyList()
            },
            description = description,
            relatedUrl = relatedUrl.ifBlank { null },
            qualification = qualification.ifBlank { null }
        )
    }
}
