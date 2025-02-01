package `in`.koreatech.koin.feature.chat.ui.model

import `in`.koreatech.koin.domain.model.chat.ChatMessage
import java.time.LocalDateTime

data class ConvertedChatMessage(
    val userId: Int,
    val userNickname: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isImage: Boolean,
    val isSentByMe: Boolean
)

fun ChatMessage.toConvertedChatMessage(): ConvertedChatMessage {
    return ConvertedChatMessage(
        userId = this.userId,
        userNickname = this.userNickname,
        content = this.content,
        timestamp = LocalDateTime.parse(this.timestamp),
        isImage = this.isImage,
        isSentByMe = this.isSentByMe
    )
}
