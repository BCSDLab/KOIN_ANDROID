package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class CartSummaryResponse(
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("shop_minimum_order_amount") val shopMinimumOrderAmount: Int,
    @SerializedName("cart_items_amount") val cartItemsAmount: Int,
    @SerializedName("is_available") val isAvailable: Boolean
)
