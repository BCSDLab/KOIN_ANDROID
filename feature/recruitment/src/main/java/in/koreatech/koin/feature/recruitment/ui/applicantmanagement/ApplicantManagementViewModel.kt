package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.recruitment.GetApplicantsUseCase
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.toApplicant
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.toMyRecruitmentPost
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

private const val APPLICANTS_PAGE_SIZE = 20

@HiltViewModel
class ApplicantManagementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getApplicantsUseCase: GetApplicantsUseCase
) : ViewModel(), ContainerHost<ApplicantManagementState, ApplicantManagementSideEffect> {

    private val postId = savedStateHandle.toRoute<RecruitmentNavType.ApplicantManagement>().postId

    override val container = container<ApplicantManagementState, ApplicantManagementSideEffect>(
        ApplicantManagementState()
    ) {
        loadApplicants()
    }

    fun loadApplicants() = intent {
        reduce { state.copy(isLoading = true) }
        getApplicantsUseCase(recruitmentId = postId, page = 1, limit = APPLICANTS_PAGE_SIZE)
            .onSuccess { applicantList ->
                reduce {
                    state.copy(
                        post = applicantList.recruitment.toMyRecruitmentPost(),
                        applicants = applicantList.applications.map { it.toApplicant() }.toImmutableList(),
                        isLoading = false
                    )
                }
            }
            .onFailure {
                reduce { state.copy(isLoading = false) }
                postSideEffect(ApplicantManagementSideEffect.Error)
            }
    }
}
