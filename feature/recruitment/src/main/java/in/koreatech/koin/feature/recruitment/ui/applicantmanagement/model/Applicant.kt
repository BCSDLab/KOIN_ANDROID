package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.recruitment.ApplicantRecruitment as DomainApplicantRecruitment
import `in`.koreatech.koin.domain.model.recruitment.ApplicantSummary as DomainApplicantSummary
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.model.toDateRange
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.toRecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus

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

fun DomainApplicantSummary.toApplicant() = Applicant(
    id = applicationId,
    name = nickname,
    role = role?.name.orEmpty(),
    department = department,
    studentNumber = studentYear.toStudentNumberLabel(),
    status = status.toApplicantStatus(),
    hasChatRoom = canOpenDirectChat
)

internal fun Int.toStudentNumberLabel(): String = "${this % 100}학번"

internal fun String.toApplicantStatus(): ApplicantStatus = when (this) {
    "ACCEPTED" -> ApplicantStatus.APPROVED
    "REJECTED" -> ApplicantStatus.REJECTED
    else -> ApplicantStatus.PENDING
}

fun DomainApplicantRecruitment.toMyRecruitmentPost() = MyRecruitmentPost(
    id = id,
    category = category.toRecruitmentCategory(),
    status = dDay?.takeIf { status == "RECRUITING" }
        ?.let { RecruitmentStatus.Recruiting(it) }
        ?: RecruitmentStatus.Complete,
    title = title,
    location = meetingType.toRecruitmentLocation(),
    dateRange = activityStartDate.toDateRange(activityEndDate),
    currentApplicants = currentParticipants,
    maxApplicants = maxParticipants
)
