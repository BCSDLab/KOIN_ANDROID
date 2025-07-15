package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubEventResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("introduce") val introduce: String,
    @SerializedName("content") val content: String?,
    @SerializedName("status") val status: String,
    @SerializedName("is_subscribed") val isSubscribed: Boolean?
)
