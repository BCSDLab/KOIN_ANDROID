package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.domain.model.chat.ChatMessage

fun ChatMessage.toChatMessageRequest() = ChatMessageRequest(
    userId = userId,
    userNickname = userNickname,
    content = content,
    timestamp = timestamp,
    isImage = isImage
)