package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.GetApplicantDetailUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.UpdateApplicationStatusUseCase
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.toUiModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private const val STATUS_ACCEPTED = "ACCEPTED"
private const val STATUS_REJECTED = "REJECTED"

@HiltViewModel
class ApplicantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getApplicantDetailUseCase: GetApplicantDetailUseCase,
    private val updateApplicationStatusUseCase: UpdateApplicationStatusUseCase
) : ViewModel(), ContainerHost<ApplicantDetailState, ApplicantDetailSideEffect> {

    private val route = savedStateHandle.toRoute<RecruitmentNavType.ApplicantDetail>()

    override val container = container<ApplicantDetailState, ApplicantDetailSideEffect>(
        ApplicantDetailState()
    ) {
        loadApplicantDetail()
    }

    fun loadApplicantDetail() = intent {
        reduce { state.copy(isLoading = true) }
        getApplicantDetailUseCase(recruitmentId = route.postId, applicationId = route.applicantId)
            .onSuccess { applicantDetail ->
                reduce { state.copy(applicant = applicantDetail.toUiModel(), isLoading = false) }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(ApplicantDetailSideEffect.Error)
            }
    }

    fun showApproveDialog() = intent {
        reduce { state.copy(showApproveDialog = true) }
    }

    fun dismissApproveDialog() = intent {
        reduce { state.copy(showApproveDialog = false) }
    }

    fun showRejectDialog() = intent {
        reduce { state.copy(showRejectDialog = true) }
    }

    fun dismissRejectDialog() = intent {
        reduce { state.copy(showRejectDialog = false) }
    }

    fun approve() = intent {
        reduce { state.copy(showApproveDialog = false) }
        updateApplicationStatusUseCase(
            recruitmentId = route.postId,
            applicationId = route.applicantId,
            status = STATUS_ACCEPTED
        )
            .onSuccess {
                getApplicantDetailUseCase(recruitmentId = route.postId, applicationId = route.applicantId)
                    .onSuccess { applicantDetail -> reduce { state.copy(applicant = applicantDetail.toUiModel()) } }
            }
            .onFailure { postSideEffect(ApplicantDetailSideEffect.Error) }
    }

    fun reject() = intent {
        reduce { state.copy(showRejectDialog = false) }
        updateApplicationStatusUseCase(
            recruitmentId = route.postId,
            applicationId = route.applicantId,
            status = STATUS_REJECTED
        )
            .onSuccess {
                getApplicantDetailUseCase(recruitmentId = route.postId, applicationId = route.applicantId)
                    .onSuccess { applicantDetail -> reduce { state.copy(applicant = applicantDetail.toUiModel()) } }
            }
            .onFailure { postSideEffect(ApplicantDetailSideEffect.Error) }
    }
}
