package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanNotificationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("message_preview")
    val messagePreview: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("post_id")
    val postId: Int,
    @SerializedName("departure")
    val departure: String,
    @SerializedName("arrival")
    val arrival: String,
    @SerializedName("departure_date")
    val departureDate: String,
    @SerializedName("departure_time")
    val departureTime: String,
    @SerializedName("current_participants")
    val currentParticipants: Int,
    @SerializedName("max_participants")
    val maxParticipants: Int,
    @SerializedName("sender_nickname")
    val senderNickname: String?,
    @SerializedName("joined_member_nickname")
    val joinedMemberNickname: String?
)
