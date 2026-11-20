package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom

fun ChatListItemResponse.toChatListItem(): ChatListItem =
    ChatListItem(
        title = title.replace("\n", " "),
        recentMessage = recentMessage,
        imageUrl = imageUrl,
        unReadMessageCount = unReadMessageCount,
        lastMessageAt = lastMessageAt,
        articleId = articleId,
        chatRoomId = chatRoomId
    )

fun ChatRoomResponse.toChatRoom(): ChatRoom =
    ChatRoom(
        articleId = articleId,
        chatRoomId = chatRoomId,
        userId = userId,
        articleTitle = articleTitle.replace("\n", " "),
        chatPartnerProfileImage = chatPartnerProfileImage
    )

fun ChatMessageResponse.toChatMessage(): ChatMessage =
    ChatMessage(
        userId = userId,
        userNickname = userNickname.orEmpty(),
        content = content,
        timestamp = timestamp,
        isImage = isImage
    )

fun ChatMessage.toChatMessageRequest(): ChatMessageRequest =
    ChatMessageRequest(
        userId = userId,
        userNickname = userNickname,
        content = content,
        timestamp = timestamp,
        isImage = isImage
    )
