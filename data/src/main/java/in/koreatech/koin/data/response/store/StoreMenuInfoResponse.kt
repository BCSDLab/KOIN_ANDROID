package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreMenuInfoResponse(
    @SerializedName("id") val shopId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_single") val isSingle: Boolean,
    @SerializedName("single_price") val singlePrice : Int,
    @SerializedName("option_prices") val optionPrices: List<OptionPrice>,
    @SerializedName("description") val description: String,
    @SerializedName("category_ids") val categoryIds: List<Int>,
    @SerializedName("image_urls") val imageUrls: List<String>
){
    data class OptionPrice(
        @SerializedName("option") val option: String,
        @SerializedName("price") val price: Int,
    )
}