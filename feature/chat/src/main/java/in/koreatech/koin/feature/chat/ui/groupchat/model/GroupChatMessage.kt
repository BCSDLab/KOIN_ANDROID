package `in`.koreatech.koin.feature.chat.ui.groupchat.model

data class GroupChatMessage(
    val id: String,
    val userId: Int,
    val userNickname: String,
    val content: String,
    val timestamp: String,
    val isImage: Boolean = false,
    val isSentByMe: Boolean = false,
    val readCount: Int = 0,
    val isFirstInGroup: Boolean = true,
    val isLeftUser: Boolean = false
)
