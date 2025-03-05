package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopRelatedListResponse(
    @SerializedName("keywords") val keywords: List<ShopRelatedResponse>,
)

data class ShopRelatedResponse(
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("shop_ids") val shopIds: List<Int>?,
    @SerializedName("shop_id") val shopId: Int?,
)
