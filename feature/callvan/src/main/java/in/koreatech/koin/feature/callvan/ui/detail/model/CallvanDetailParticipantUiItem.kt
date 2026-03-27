package `in`.koreatech.koin.feature.callvan.ui.detail.model

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail

@Immutable
data class CallvanDetailParticipantUiItem(
    val id: Int,
    val name: String,
    val isMe: Boolean,
    val isReported: Boolean
)

fun CallvanPostDetail.CallvanParticipant.toUiItem() = CallvanDetailParticipantUiItem(
    id = userId,
    name = nickname,
    isMe = isMe,
    isReported = isReported
)
