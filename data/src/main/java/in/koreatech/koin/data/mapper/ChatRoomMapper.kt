package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.chat.ChatRoomInfoResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomMessageResponse
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage

fun ChatRoomMessageResponse.toChatRoomMessage(): ChatRoomMessage = ChatRoomMessage(
    messageId = messageId,
    userId = userId,
    userNickname = userNickname,
    content = content,
    timestamp = timestamp,
    isImage = isImage,
    unreadCount = unreadCount,
    isLeftUser = isLeftUser
)

fun ChatRoomInfoResponse.toChatRoomInfo(): ChatRoomInfo = ChatRoomInfo(
    roomId = roomId,
    domainType = ChatDomainType.valueOf(domainType),
    contextId = contextId,
    chatRoomId = chatRoomId,
    roomType = roomType,
    canSend = canSend,
    supportsReadReceipts = supportsReadReceipts,
    roomName = roomName,
    imageUrl = imageUrl,
    memberCount = memberCount,
    maxMemberCount = maxMemberCount,
    counterpartId = counterpartId,
    counterpartNickname = counterpartNickname,
    counterpartProfileImage = counterpartProfileImage,
    unreadMessageCount = unreadMessageCount,
    lastMessageId = lastMessageId,
    lastMessageIsImage = lastMessageIsImage,
    lastMessageContent = lastMessageContent,
    lastMessageAt = lastMessageAt
)
