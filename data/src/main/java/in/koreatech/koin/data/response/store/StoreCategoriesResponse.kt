package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreCategoriesResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("shop_categories") val shop_categories: List<StoreCategoriesItemResponse>
)
