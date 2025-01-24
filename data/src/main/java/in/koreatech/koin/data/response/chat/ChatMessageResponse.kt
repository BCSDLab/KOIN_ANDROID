package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_nickname") val userNickname: String,
    @SerializedName("content") val content: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("is_image") val isImage: Boolean
) {
    fun toChatMessage() = ChatMessage(
        userId = userId,
        userNickname = userNickname,
        content = content,
        timestamp = timestamp,
        isImage = isImage,
        isSentByMe = false
    )
}