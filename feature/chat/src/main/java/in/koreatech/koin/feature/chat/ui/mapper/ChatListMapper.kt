package `in`.koreatech.koin.feature.chat.ui.mapper

import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo

fun List<ChatRoomInfo>.toChatListItems(): List<ChatListItem> = mapNotNull { it.toChatListItem() }

private fun ChatRoomInfo.toChatListItem(): ChatListItem? {
    val messageAt = lastMessageAt ?: return null
    val legacyChatRoomId = chatRoomId ?: return null
    return ChatListItem(
        title = roomName.replace("\n", " "),
        recentMessage = lastMessageContent.orEmpty(),
        imageUrl = imageUrl,
        unReadMessageCount = unreadMessageCount,
        lastMessageAt = messageAt,
        articleId = contextId,
        chatRoomId = legacyChatRoomId
    )
}
