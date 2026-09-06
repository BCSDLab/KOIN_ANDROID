package `in`.koreatech.koin.domain.model.recruitment

data class MyAppliedRecruitment(
    val applicationId: Int,
    val status: String,
    val teamChatAvailable: Boolean,
    val teamChatRoomId: Int?,
    val directChatRoomId: Int?,
    val role: TeamRecruitmentApplicationRole?,
    val recruitment: Recruitment
)
