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
    DIRECT,
    UNKNOWN;

    companion object {
        fun fromString(value: String): RecruitmentChatRoomType =
            entries.find { it.name == value } ?: UNKNOWN
    }
}

enum class RecruitmentChatRoomStatus {
    ACTIVE,
    READ_ONLY,
    UNKNOWN;

    companion object {
        fun fromString(value: String): RecruitmentChatRoomStatus =
            entries.find { it.name == value } ?: UNKNOWN
    }
}

data class RecruitmentChatCounterpart(
    val id: Int,
    val nickname: String
)
