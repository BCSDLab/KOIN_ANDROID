package `in`.koreatech.koin.feature.callvan.ui.detail.model

import androidx.compose.runtime.Immutable

@Immutable
data class CallvanDetailParticipantUiItem(
    val id: Int,
    val name: String,
    val isMe: Boolean
)
