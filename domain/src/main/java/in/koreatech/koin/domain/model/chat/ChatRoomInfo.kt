package `in`.koreatech.koin.domain.model.chat

data class ChatRoomInfo(
    val roomId: String,
    val domainType: ChatDomainType,
    val contextId: Int,
    val chatRoomId: Int?,
    val roomType: String,
    val canSend: Boolean,
    val supportsReadReceipts: Boolean,
    val roomName: String,
    val imageUrl: String?,
    val memberCount: Int?,
    val maxMemberCount: Int?,
    val counterpartId: Int?,
    val counterpartNickname: String?,
    val counterpartProfileImage: String?,
    val unreadMessageCount: Int,
    val lastMessageId: String?,
    val lastMessageIsImage: Boolean?,
    val lastMessageContent: String?,
    val lastMessageAt: String?
)
