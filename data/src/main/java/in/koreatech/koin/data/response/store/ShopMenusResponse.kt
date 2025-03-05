package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopMenusResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("is_single") val isSingle: Boolean,
    @SerializedName("single_price") val singlePrice: Int?,
    @SerializedName("option_prices") val optionPrices: List<ShopMenuOptionsResponse>?,
    @SerializedName("description") val description: String?,
    @SerializedName("image_urls") val imageUrls: List<String>?,
)
