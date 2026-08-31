package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentPost(
    val id: Int,
    val category: String,
    val title: String,
    val meetingType: String,
    val activityStartDate: String,
    val activityEndDate: String,
    val deadlineDate: String,
    val dDay: Int,
    val status: String,
    val recruitmentType: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val roles: List<RecruitmentRole>
)

data class RecruitmentRole(
    val id: Int,
    val name: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val isClosed: Boolean
)
