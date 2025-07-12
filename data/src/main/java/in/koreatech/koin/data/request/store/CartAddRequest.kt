package `in`.koreatech.koin.data.request.store

import com.google.gson.annotations.SerializedName

data class CartAddRequest(
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("orderable_shop_menu_id") val orderableShopMenuId: Int,
    @SerializedName("orderable_shop_menu_price_id") val orderableShopMenuPriceId: Int,
    @SerializedName("orderable_shop_menu_option_ids") val orderableShopMenuOptionIds: List<CartAddOptionRequest>?
) {
    data class CartAddOptionRequest(
        @SerializedName("option_group_id") val optionGroupId: Int,
        @SerializedName("option_id") val optionId: Int
    )
}
