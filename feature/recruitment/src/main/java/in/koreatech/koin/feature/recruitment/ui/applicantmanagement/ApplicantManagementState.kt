package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.Applicant
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ApplicantManagementState(
    val post: MyRecruitmentPost? = null,
    val applicants: ImmutableList<Applicant> = persistentListOf(),
    val isLoading: Boolean = false
)
