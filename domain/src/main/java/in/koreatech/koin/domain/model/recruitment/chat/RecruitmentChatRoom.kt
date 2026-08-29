package `in`.koreatech.koin.domain.model.recruitment.chat

data class RecruitmentChatRoom(
    val chatRoomId: Int,
    val roomName: String,
    val roomType: RecruitmentChatRoomType,
    val status: RecruitmentChatRoomStatus,
    val memberCount: Int,
    val maxMemberCount: Int,
    val counterpart: RecruitmentChatCounterpart?
)

enum class RecruitmentChatRoomType {
    TEAM,
    DIRECT
}

enum class RecruitmentChatRoomStatus {
    ACTIVE,
    READ_ONLY
}

data class RecruitmentChatCounterpart(
    val id: Int,
    val nickname: String
)
