package `in`.koreatech.koin.feature.chat.ui.mapper

import androidx.core.net.toUri
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessages
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomState
import java.time.LocalDate
import java.time.LocalDateTime

private const val UNKNOWN_USER_ID = -1

fun ChatRoomMessage.toConvertedChatMessage(currentUserId: Int): ConvertedChatMessage {
    val senderId = userId ?: UNKNOWN_USER_ID
    return ConvertedChatMessage(
        userId = senderId,
        userNickname = userNickname.orEmpty(),
        content = content,
        timestamp = LocalDateTime.parse(timestamp),
        isImage = isImage,
        isSentByMe = userId != null && userId == currentUserId,
        uploadId = messageId
    )
}

fun List<ChatRoomMessage>.mapToConvertedChatMessages(currentUserId: Int): List<ConvertedChatMessages> =
    if (isEmpty()) {
        listOf(ConvertedChatMessages(LocalDate.now(), emptyList()))
    } else {
        map { it.toConvertedChatMessage(currentUserId) }.toDateGroups()
    }

fun List<ConvertedChatMessages>.appendMessage(
    message: ChatRoomMessage,
    currentUserId: Int
): List<ConvertedChatMessages> = mergeWithChatMessages(listOf(message), currentUserId)

fun List<ConvertedChatMessages>.mergeWithChatMessages(
    messages: List<ChatRoomMessage>,
    currentUserId: Int
): List<ConvertedChatMessages> {
    if (messages.isEmpty()) return this

    val messagesById = linkedMapOf<String, ConvertedChatMessage>()
    flatMap { it.messages }.forEach { messagesById[it.uploadId] = it }
    messages.forEach { message ->
        val converted = message.toConvertedChatMessage(currentUserId)
        messagesById[converted.uploadId] = converted
    }
    return messagesById.values.sortedBy { it.timestamp }.toDateGroups()
}

private fun List<ConvertedChatMessage>.toDateGroups(): List<ConvertedChatMessages> =
    groupBy { it.timestamp.toLocalDate() }
        .toSortedMap()
        .map { (date, messages) -> ConvertedChatMessages(date, messages) }

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

fun ChatRoomInfo.toChatRoomState(user: User): ChatRoomState {
    val currentUserId = when (user) {
        is User.Student -> user.id
        is User.General -> user.id
        User.Anonymous -> throw IllegalAccessException("Anonymous users cannot access chat rooms")
    }
    return ChatRoomState(
        articleId = contextId,
        chatRoomId = requireNotNull(chatRoomId) { "chatRoomId is required for legacy ChatRoomState" },
        userId = currentUserId,
        articleTitle = roomName.replace("\n", " "),
        chatPartnerProfileImage = (counterpartProfileImage ?: "").toUri()
    )
}
