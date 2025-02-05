package `in`.koreatech.koin.data.request.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    @SerialName("user_id") val userId: Int,
    @SerialName("user_nickname") val userNickname: String,
    @SerialName("content") val content: String,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("is_image") val isImage: Boolean,
    @SerialName("isSentByMe") val isSentByMe: Boolean
)
