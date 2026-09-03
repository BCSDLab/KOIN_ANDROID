package `in`.koreatech.koin.domain.model.recruitment

data class MyAppliedRecruitment(
    val applicationId: Int,
    val status: String,
    val teamChatAvailable: Boolean,
    val teamChatRoomId: Int?,
    val directChatRoomId: Int?,
    val roleName: String,
    val recruitment: Recruitment
)
