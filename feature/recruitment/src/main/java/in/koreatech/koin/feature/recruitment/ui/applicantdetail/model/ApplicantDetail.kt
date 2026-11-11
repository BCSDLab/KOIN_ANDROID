package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.recruitment.ApplicantDetail as DomainApplicantDetail
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivity
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.toApplicantStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ApplicantDetail(
    val id: Int,
    val name: String,
    val role: String,
    val department: String,
    val studentNumber: Int,
    val status: ApplicantStatus,
    val skills: ImmutableList<String>,
    val activities: ImmutableList<ApplicantActivity>,
    val introduction: String,
    val motivation: String,
    val availableTime: String,
    val canDecide: Boolean = true
)

@Immutable
data class ApplicantActivity(
    val title: String,
    val startedAt: String,
    val endedAt: String?,
    val content: String
)

fun DomainApplicantDetail.toUiModel(): ApplicantDetail {
    val profile = profileSnapshot
    return ApplicantDetail(
        id = applicationId,
        name = profile.nickname,
        role = role?.name ?: profile.preferredRole,
        department = profile.department,
        studentNumber = profile.studentYear % 100,
        status = status.toApplicantStatus(),
        skills = profile.skills.toImmutableList(),
        activities = profile.activities.map { it.toApplicantActivity() }.toImmutableList(),
        introduction = profile.selfIntroduction,
        motivation = motivation,
        availableTime = availability,
        canDecide = canDecide
    )
}

private fun TeamRecruitmentActivity.toApplicantActivity() = ApplicantActivity(
    title = title,
    startedAt = startedAt.toDotDate(),
    endedAt = endedAt?.toDotDate(),
    content = description
)

private fun String.toDotDate(): String = replace("-", ".")
