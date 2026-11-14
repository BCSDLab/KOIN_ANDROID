package `in`.koreatech.koin.data.response.chat

import com.google.gson.annotations.SerializedName

data class ChatRoomInfoResponse(
    @SerializedName("room_id") val roomId: String,
    @SerializedName("domain_type") val domainType: String,
    @SerializedName("context_id") val contextId: Int,
    @SerializedName("chat_room_id") val chatRoomId: Int?,
    @SerializedName("room_type") val roomType: String,
    @SerializedName("can_send") val canSend: Boolean,
    @SerializedName("supports_read_receipts") val supportsReadReceipts: Boolean,
    @SerializedName("room_name") val roomName: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("member_count") val memberCount: Int?,
    @SerializedName("max_member_count") val maxMemberCount: Int?,
    @SerializedName("counterpart_id") val counterpartId: Int?,
    @SerializedName("counterpart_nickname") val counterpartNickname: String?,
    @SerializedName("counterpart_profile_image") val counterpartProfileImage: String?,
    @SerializedName("unread_message_count") val unreadMessageCount: Int,
    @SerializedName("last_message_id") val lastMessageId: String?,
    @SerializedName("last_message_is_image") val lastMessageIsImage: Boolean?,
    @SerializedName("last_message_content") val lastMessageContent: String?,
    @SerializedName("last_message_at") val lastMessageAt: String?
)
