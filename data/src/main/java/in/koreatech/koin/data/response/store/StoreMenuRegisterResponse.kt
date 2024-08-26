package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice

data class StoreMenuRegisterResponse(
    @SerializedName("category_ids") val menuCategoryId: List<Int>,
    @SerializedName("description") val description: String,
    @SerializedName("image_urls") val imageUrls: List<String>,
    @SerializedName("is_single") val isSingle: Boolean,
    @SerializedName("name") val name: String,
    @SerializedName("option_prices") val optionPrices: List<OptionPrice>?,
    @SerializedName("single_price") val singlePrice: Int?
){
    data class OptionPrice(
        @SerializedName("option") val option: String?,
        @SerializedName("price") val price: Int?,
    )
}