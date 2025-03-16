package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerializedName("user_id")
    @SerialName("user_id")
    val userId: Int,
    @SerializedName("user_nickname")
    @SerialName("user_nickname")
    val userNickname: String,
    @SerializedName("content")
    @SerialName("content")
    val content: String,
    @SerializedName("timestamp")
    @SerialName("timestamp")
    val timestamp: String,
    @SerializedName("is_image")
    @SerialName("is_image")
    val isImage: Boolean
) {
    fun toChatMessage() =
        ChatMessage(
            userId = userId,
            userNickname = userNickname,
            content = content,
            timestamp = timestamp,
            isImage = isImage
        )
}
