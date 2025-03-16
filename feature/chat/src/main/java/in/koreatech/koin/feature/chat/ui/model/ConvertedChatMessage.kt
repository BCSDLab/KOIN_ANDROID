package `in`.koreatech.koin.feature.chat.ui.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConvertedChatMessage(
    val userId: Int,
    val userNickname: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isImage: Boolean,
    val isSentByMe: Boolean
) : Parcelable

fun ChatMessage.toConvertedChatMessage(userId: Int): ConvertedChatMessage {
    return ConvertedChatMessage(
        userId = this.userId,
        userNickname = this.userNickname,
        content = this.content,
        timestamp = LocalDateTime.parse(this.timestamp),
        isImage = this.isImage,
        isSentByMe = userId == this.userId
    )
}
