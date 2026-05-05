package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.callvan.CallvanChatMessageResponse
import `in`.koreatech.koin.data.response.callvan.CallvanNotificationResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostCreateResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostDetailResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostSearchResponse
import `in`.koreatech.koin.data.response.callvan.CallvanRestrictionResponse
import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.model.callvan.CallvanPostCreate
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch
import `in`.koreatech.koin.domain.model.callvan.CallvanRestriction

fun CallvanPostCreateResponse.toCallvanPostCreate() = CallvanPostCreate(
    id = id,
    author = author,
    departureType = departureType,
    departureCustomName = departureCustomName,
    arrivalType = arrivalType,
    arrivalCustomName = arrivalCustomName,
    departureDate = departureDate,
    departureTime = departureTime,
    maxParticipants = maxParticipants,
    currentParticipants = currentParticipants,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CallvanPostSearchResponse.toCallvanPostSearch() = CallvanPostSearch(
    posts = posts.map { it.toCallvanPost() },
    totalCount = totalCount,
    currentPage = currentPage,
    totalPage = totalPage
)

fun CallvanPostSearchResponse.CallvanPostResponse.toCallvanPost() = CallvanPostSearch.CallvanPost(
    id = id,
    title = title,
    departure = departure,
    arrival = arrival,
    departureDate = departureDate,
    departureTime = departureTime,
    authorNickname = authorNickname,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    status = status,
    isJoined = isJoined,
    isAuthor = isAuthor
)

fun CallvanChatMessageResponse.toCallvanChatMessage() = CallvanChatMessage(
    roomName = roomName,
    messages = messages.map { it.toCallvanMessage() }
)

fun CallvanChatMessageResponse.CallvanMessageDto.toCallvanMessage() = CallvanChatMessage.CallvanMessage(
    userId = userId,
    senderNickname = senderNickname,
    content = content,
    date = date,
    time = time,
    isImage = isImage,
    isLeftUser = isLeftUser,
    isMine = isMine
)

fun CallvanPostDetailResponse.toCallvanPostDetail() = CallvanPostDetail(
    id = id,
    title = title,
    departure = departure,
    arrival = arrival,
    departureDate = departureDate,
    departureTime = departureTime,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    status = status,
    participants = participants.map { it.toCallvanParticipant() }
)

fun CallvanPostDetailResponse.CallvanParticipantResponse.toCallvanParticipant() = CallvanPostDetail.CallvanParticipant(
    userId = userId,
    nickname = nickname,
    isMe = isMe,
    isReported = isReported
)

fun CallvanRestrictionResponse.toCallvanRestriction() = CallvanRestriction(
    isRestricted = isRestricted,
    restrictionType = CallvanRestriction.CallvanRestrictionType.from(restrictionType),
    restrictedUntil = restrictedUntil
)

fun CallvanNotificationResponse.toCallvanNotification() = CallvanNotification(
    id = id,
    type = type,
    messagePreview = messagePreview,
    isRead = isRead,
    createdAt = createdAt,
    postId = postId,
    departure = departure,
    arrival = arrival,
    departureDate = departureDate,
    departureTime = departureTime,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    senderNickname = senderNickname,
    joinedMemberNickname = joinedMemberNickname
)
