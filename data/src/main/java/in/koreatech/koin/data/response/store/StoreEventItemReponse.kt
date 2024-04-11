package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreEventItemReponse(
    @SerializedName("shop_id") val shop_id: Int,
    @SerializedName("shop_name") val shop_name: String,
    @SerializedName("event_id") val event_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("thumbnail_images") val thumbnail_images: List<String>,
    @SerializedName("start_date") val start_date: String,
    @SerializedName("end_date") val end_date: String
)