package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class DiningResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("place") val place: String,
    @SerializedName("price_card") val priceCard: Int?,
    @SerializedName("price_cash") val priceCash: Int?,
    @SerializedName("kcal") val kcal: Int?,
    @SerializedName("menu") val menu: List<String>,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("sold_out") val isSoldOut: Boolean,
    // TODO: sold_out 품절 시각으로 변경시 대응
//    @SerializedName("sold_out") val soldOutAt: String?,
    @SerializedName("is_changed") val isChanged: Boolean,
    @SerializedName("error") val error: String?
)