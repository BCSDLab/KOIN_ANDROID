package `in`.koreatech.koin.feature.callvan.ui.detail.model

import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail

data class CallvanDetailParticipantUiItem(
    val id: Int,
    val name: String,
    val isMe: Boolean
)

fun CallvanPostDetail.CallvanParticipant.toUiItem() = CallvanDetailParticipantUiItem(
    id = userId,
    name = nickname,
    isMe = isMe
)
