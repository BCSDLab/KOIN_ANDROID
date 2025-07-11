package `in`.koreatech.koin.data.request.store

import com.google.gson.annotations.SerializedName

data class CartItemRequest(
    @SerializedName("orderable_shop_menu_price_id") val orderableShopMenuPriceId: Int,
    @SerializedName("options") val options: List<CartItemOptionRequest>
) {
    data class CartItemOptionRequest(
        @SerializedName("option_group_id") val optionGroupId: Int,
        @SerializedName("option_id") val optionId: Int
    )
}
