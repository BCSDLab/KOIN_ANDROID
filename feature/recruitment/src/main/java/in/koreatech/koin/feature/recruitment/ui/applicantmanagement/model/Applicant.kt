package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus

@Immutable
data class Applicant(
    val id: Int,
    val name: String,
    val role: String,
    val department: String,
    val studentNumber: String,
    val status: ApplicantStatus,
    val hasChatRoom: Boolean = false
)
