package `in`.koreatech.koin.feature.chat.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isSentByMe: Boolean,
    val authorNickname: String = "",
    val isFirstInGroup: Boolean = true,
    val isImage: Boolean = false,
    val userId: Int? = null,
    val isLeftUser: Boolean = false
)
