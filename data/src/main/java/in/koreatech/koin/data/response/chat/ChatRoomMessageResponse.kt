package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomMessageResponse(
    @SerializedName("message_id") @SerialName("message_id") val messageId: String,
    @SerializedName("user_id") @SerialName("user_id") val userId: Int? = null,
    @SerializedName("user_nickname") @SerialName("user_nickname") val userNickname: String? = null,
    @SerializedName("content") @SerialName("content") val content: String,
    @SerializedName("timestamp") @SerialName("timestamp") val timestamp: String,
    @SerializedName("is_image") @SerialName("is_image") val isImage: Boolean,
    @SerializedName("unread_count") @SerialName("unread_count") val unreadCount: Int? = null,
    @SerializedName("is_left_user") @SerialName("is_left_user") val isLeftUser: Boolean
)
