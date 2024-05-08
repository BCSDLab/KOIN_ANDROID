package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreDetailEventResponse(
    @SerializedName("events") val events: List<StoreEventDTO>?,
){
    data class StoreEventDTO(
        @SerializedName("shop_id") val shopId: Int?,
        @SerializedName("shop_name") val shopName: String?,
        @SerializedName("event_id") val eventId: Int?,
        @SerializedName("title") val title: String?,
        @SerializedName("content") val content: String?,
        @SerializedName("thumbnail_images") val thumbnailImages: List<String>?,
        @SerializedName("start_date") val startDate: String?,
        @SerializedName("end_date") val endDate: String?,
    )
}