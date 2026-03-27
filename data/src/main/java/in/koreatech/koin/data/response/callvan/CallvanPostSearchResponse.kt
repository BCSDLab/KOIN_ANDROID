package `in`.koreatech.koin.data.response.callvan

import com.google.gson.annotations.SerializedName

data class CallvanPostSearchResponse(
    @SerializedName("posts")
    val posts: List<CallvanPostResponse>,
    @SerializedName("total_count")
    val totalCount: Long,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_page")
    val totalPage: Int
) {
    data class CallvanPostResponse(
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
        @SerializedName("author_nickname")
        val authorNickname: String?,
        @SerializedName("current_participants")
        val currentParticipants: Int,
        @SerializedName("max_participants")
        val maxParticipants: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("is_joined")
        val isJoined: Boolean,
        @SerializedName("is_author")
        val isAuthor: Boolean
    )
}
