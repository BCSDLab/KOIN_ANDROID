package `in`.koreatech.koin.data.response.recruitment.chat

import com.google.gson.annotations.SerializedName

data class RecruitmentChatMessageResponse(
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_nickname")
    val userNickname: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("is_image")
    val isImage: Boolean,
    @SerializedName("unread_count")
    val unreadCount: Int
)
