package `in`.koreatech.koin.domain.model.recruitment

data class RecruitmentDetail(
    val id: Int,
    val category: String,
    val title: String,
    val meetingType: String,
    val activityStartDate: String,
    val activityEndDate: String,
    val deadlineDate: String,
    val dDay: Int?,
    val status: String,
    val recruitmentType: String,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val roles: List<RecruitmentRole>,
    val authorNickname: String?,
    val description: String,
    val relatedUrl: String?,
    val qualification: String?,
    val createdAt: String,
    val isAuthor: Boolean,
    val canApply: Boolean,
    val applyBlockReason: String?,
    val application: RecruitmentApplication?,
    val canManageApplicants: Boolean,
    val teamChatAvailable: Boolean,
    val teamChatRoomId: Int?
)

data class RecruitmentApplication(
    val applicationId: Int,
    val status: String
)
