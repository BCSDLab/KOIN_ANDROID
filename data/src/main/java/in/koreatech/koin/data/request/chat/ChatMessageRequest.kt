package `in`.koreatech.koin.data.request.chat

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_nickname") val userNickname: String,
    @SerializedName("content") val content: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("is_image") val isImage: Boolean,
    @SerializedName("isSentByMe") val isSentByMe: Boolean
)
