package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.ApplicantDetail

@Immutable
data class ApplicantDetailState(
    val applicant: ApplicantDetail? = null,
    val isLoading: Boolean = false,
    val showApproveDialog: Boolean = false,
    val showRejectDialog: Boolean = false
)
