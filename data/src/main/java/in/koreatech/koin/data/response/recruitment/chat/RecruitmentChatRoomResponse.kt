package `in`.koreatech.koin.data.response.recruitment.chat

import com.google.gson.annotations.SerializedName

data class RecruitmentChatRoomResponse(
    @SerializedName("chat_room_id")
    val chatRoomId: Int,
    @SerializedName("room_name")
    val roomName: String,
    @SerializedName("room_type")
    val roomType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("member_count")
    val memberCount: Int,
    @SerializedName("max_member_count")
    val maxMemberCount: Int,
    @SerializedName("counterpart")
    val counterpart: RecruitmentChatCounterpartResponse?
) {
    data class RecruitmentChatCounterpartResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("nickname")
        val nickname: String
    )
}
