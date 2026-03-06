package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanChatMessageResponse(
    @SerializedName("room_name")
    val roomName: String,
    @SerializedName("messages")
    val messages: List<CallvanMessageDto>
) {
    data class CallvanMessageDto(
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("sender_nickname")
        val senderNickname: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("time")
        val time: String,
        @SerializedName("is_image")
        val isImage: Boolean,
        @SerializedName("is_left_user")
        val isLeftUser: Boolean,
        @SerializedName("is_mine")
        val isMine: Boolean
    )
}
