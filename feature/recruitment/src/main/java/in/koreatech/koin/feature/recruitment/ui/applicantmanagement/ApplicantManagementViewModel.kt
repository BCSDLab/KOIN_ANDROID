package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.Applicant
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ApplicantManagementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ApplicantManagementState, ApplicantManagementSideEffect> {

    private val postId = savedStateHandle.toRoute<RecruitmentNavType.ApplicantManagement>().postId

    override val container = container<ApplicantManagementState, ApplicantManagementSideEffect>(
        ApplicantManagementState(
            post = MyRecruitmentPost(
                id = postId,
                category = RecruitmentCategory.CONTEST,
                status = RecruitmentStatus.Recruiting(daysLeft = 5),
                title = "AI 아이디어 공모전 팀원 모집",
                location = "온라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 0,
                maxApplicants = 3,
                teamChatRoomId = 1
            ),
            applicants = persistentListOf(
                Applicant(
                    id = 1,
                    name = "김철수",
                    role = "프론트엔드",
                    department = "컴퓨터공학부",
                    studentNumber = "23학번",
                    status = ApplicantStatus.PENDING
                )
            )
        )
    )
}
