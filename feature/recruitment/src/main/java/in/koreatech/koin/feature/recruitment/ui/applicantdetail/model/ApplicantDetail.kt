package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ApplicantDetail(
    val id: Int,
    val name: String,
    val role: String,
    val department: String,
    val studentNumber: String,
    val status: ApplicantStatus,
    val skills: ImmutableList<String>,
    val activities: ImmutableList<ApplicantActivity>,
    val introduction: String,
    val motivation: String,
    val availableTime: String
)

@Immutable
data class ApplicantActivity(
    val title: String,
    val period: String,
    val content: String
)
