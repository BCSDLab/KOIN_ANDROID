package `in`.koreatech.koin.feature.recruitment.ui.chat.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecruitmentChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isSentByMe: Boolean,
    val authorNickname: String = "",
    val isFirstInGroup: Boolean = true
)
