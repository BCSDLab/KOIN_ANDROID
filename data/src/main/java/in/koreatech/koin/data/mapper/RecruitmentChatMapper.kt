package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatMessageResponse
import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatRoomResponse
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatCounterpart
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoom
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoomStatus
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoomType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

private val CHAT_TIMESTAMP_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
    .toFormatter()

fun RecruitmentChatRoomResponse.toRecruitmentChatRoom(): RecruitmentChatRoom = RecruitmentChatRoom(
    chatRoomId = chatRoomId,
    roomName = roomName,
    roomType = RecruitmentChatRoomType.fromString(roomType),
    status = RecruitmentChatRoomStatus.fromString(status),
    memberCount = memberCount,
    maxMemberCount = maxMemberCount,
    counterpart = counterpart?.let {
        RecruitmentChatCounterpart(id = it.id, nickname = it.nickname)
    }
)

fun RecruitmentChatMessageResponse.toRecruitmentChatMessage(): RecruitmentChatMessage = RecruitmentChatMessage(
    messageId = messageId,
    userId = userId,
    userNickname = userNickname,
    content = content,
    timestamp = LocalDateTime.parse(timestamp, CHAT_TIMESTAMP_FORMATTER),
    isImage = isImage,
    unreadCount = unreadCount
)
