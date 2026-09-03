package `in`.koreatech.koin.feature.recruitment.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.DeleteRecruitmentUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.GetRecruitmentDetailUseCase
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRoleModel
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentType
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentDisplayDate
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class RecruitmentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecruitmentDetailUseCase: GetRecruitmentDetailUseCase,
    private val deleteRecruitmentUseCase: DeleteRecruitmentUseCase
) : ViewModel(), ContainerHost<RecruitmentDetailState, RecruitmentDetailSideEffect> {

    private val postId =
        savedStateHandle.toRoute<RecruitmentNavType.RecruitmentDetail>().postId

    override val container = container<RecruitmentDetailState, RecruitmentDetailSideEffect>(
        RecruitmentDetailState(id = postId)
    ) {
        fetchRecruitmentDetail()
    }

    fun fetchRecruitmentDetail() = intent {
        reduce { state.copy(isLoading = true) }
        getRecruitmentDetailUseCase(recruitmentId = postId)
            .onSuccess { detail ->
                reduce {
                    state.copy(
                        id = detail.id,
                        category = RecruitmentCategory.from(detail.category),
                        title = detail.title,
                        location = RecruitmentLocation.from(detail.meetingType),
                        activityStartDate = detail.activityStartDate.toRecruitmentDisplayDate(),
                        activityEndDate = detail.activityEndDate.toRecruitmentDisplayDate(),
                        dDay = detail.dDay,
                        status = RecruitmentStatus.from(detail.status),
                        recruitmentType = RecruitmentType.from(detail.recruitmentType),
                        currentParticipants = detail.currentParticipants,
                        maxParticipants = detail.maxParticipants,
                        roles = detail.roles.map { role ->
                            RecruitmentRoleModel(
                                id = role.id,
                                name = role.name,
                                currentParticipants = role.currentParticipants,
                                maxParticipants = role.maxParticipants,
                                isClosed = role.isClosed
                            )
                        }.toImmutableList(),
                        authorNickname = detail.authorNickname,
                        description = detail.description,
                        qualification = detail.qualification.orEmpty(),
                        createdAt = detail.createdAt.toRecruitmentDisplayDate(),
                        isAuthor = detail.isAuthor,
                        isLoading = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentDetailSideEffect.ShowLoadError)
            }
    }

    fun deleteRecruitment() = intent {
        reduce { state.copy(isDeleteDialogVisible = false, isLoading = true) }
        deleteRecruitmentUseCase(recruitmentId = postId)
            .onSuccess {
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentDetailSideEffect.DeleteSuccess)
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(RecruitmentDetailSideEffect.ShowDeleteError)
            }
    }

    fun updateMoreMenuVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(isMoreMenuVisible = visible) }
    }

    fun updateDeleteDialogVisible(visible: Boolean) = blockingIntent {
        reduce { state.copy(isDeleteDialogVisible = visible, isMoreMenuVisible = false) }
    }
}
