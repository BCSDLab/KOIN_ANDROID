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
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("error") val error: String?
)