package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentUpdate(
    val category: String,
    val title: String,
    val meetingType: String,
    val activityStartDate: String,
    val activityEndDate: String,
    val deadlineDate: String,
    val recruitmentType: String,
    val maxParticipants: Int?,
    val roles: List<RecruitmentUpdateRole>,
    val description: String,
    val relatedUrl: String?,
    val qualification: String?
)
