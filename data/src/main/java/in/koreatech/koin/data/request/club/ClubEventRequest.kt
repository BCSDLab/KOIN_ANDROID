package `in`.koreatech.koin.data.request.club

import com.google.gson.annotations.SerializedName

data class ClubEventRequest(
    @SerializedName("name") val name: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("introduce") val introduce: String,
    @SerializedName("content") val content: String?
)
