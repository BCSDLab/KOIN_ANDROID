package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.ApplicantActivity
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.ApplicantDetail
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ApplicantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ApplicantDetailState, ApplicantDetailSideEffect> {

    private val route = savedStateHandle.toRoute<RecruitmentNavType.ApplicantDetail>()

    override val container = container<ApplicantDetailState, ApplicantDetailSideEffect>(
        ApplicantDetailState(
            applicant = ApplicantDetail(
                id = route.applicantId,
                name = "김철수",
                role = "프론트엔드",
                department = "컴퓨터공학부",
                studentNumber = "23학번",
                status = ApplicantStatus.PENDING,
                skills = persistentListOf("정보처리기사"),
                activities = persistentListOf(
                    ApplicantActivity(
                        title = "AI 공모전",
                        period = "2026.03.23 - 2026.04.06",
                        content = "AI 공모전에서 기획을 담당했고 @@@를 주제로 @@@를 만들었습니다"
                    )
                ),
                introduction = "안녕하세요.",
                motivation = "안녕하세요.",
                availableTime = "월 수 금 20시 이후"
            )
        )
    )

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
        reduce {
            state.copy(
                showApproveDialog = false,
                applicant = state.applicant?.copy(status = ApplicantStatus.APPROVED)
            )
        }
    }

    fun reject() = intent {
        reduce {
            state.copy(
                showRejectDialog = false,
                applicant = state.applicant?.copy(status = ApplicantStatus.REJECTED)
            )
        }
    }
}
