package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerEventResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("thumbnail_images") val thumbnailImages: List<String>?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?
)
