package `in`.koreatech.koin.data.response.recruitment

import com.google.gson.annotations.SerializedName

data class RecruitmentNotificationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("target_type")
    val targetType: String,
    @SerializedName("message_preview")
    val messagePreview: String,
    @SerializedName("sender_nickname")
    val senderNickname: String?,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("recruitment_id")
    val recruitmentId: Int,
    @SerializedName("application_id")
    val applicationId: Int?,
    @SerializedName("chat_room_id")
    val chatRoomId: Int?
)
