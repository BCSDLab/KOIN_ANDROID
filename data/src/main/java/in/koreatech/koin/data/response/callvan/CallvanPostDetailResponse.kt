package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanPostDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
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
    @SerializedName("status")
    val status: String,
    @SerializedName("participants")
    val participants: List<CallvanParticipantResponse>
) {
    data class CallvanParticipantResponse(
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("is_me")
        val isMe: Boolean
    )
}
