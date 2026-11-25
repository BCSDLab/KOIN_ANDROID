package `in`.koreatech.koin.feature.chat.ui.mapper

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessage
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessageGroup
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val CHAT_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun List<ChatRoomMessage>.toChatMessageGroups(
    currentUserId: Int
): ImmutableList<ChatMessageGroup> {
    val groups = linkedMapOf<LocalDate, MutableList<ChatMessage>>()
    for (message in this) {
        val timestamp = LocalDateTime.parse(message.timestamp)
        val dayMessages = groups.getOrPut(timestamp.toLocalDate()) { mutableListOf() }
        val previousMessage = dayMessages.lastOrNull()
        dayMessages.add(
            ChatMessage(
                id = message.messageId,
                content = message.content,
                timestamp = timestamp.format(CHAT_TIME_FORMATTER),
                isSentByMe = message.userId != null && message.userId == currentUserId,
                authorNickname = message.userNickname.orEmpty(),
                isImage = message.isImage,
                userId = message.userId,
                isLeftUser = message.isLeftUser,
                isFirstInGroup = previousMessage == null || message.userId == null || previousMessage.userId != message.userId
            )
        )
    }
    return groups.map { (date, dayMessages) ->
        ChatMessageGroup(date = date, messages = dayMessages.toImmutableList())
    }.toImmutableList()
}
