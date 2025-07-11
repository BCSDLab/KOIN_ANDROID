package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("shop_name") val shopName: String,
    @SerializedName("shop_thumbnail_image_url") val shopThumbnailImageUrl: String,
    @SerializedName("orderable_shop_id") val orderableShopId: Int,
    @SerializedName("is_delivery_available") val isDeliveryAvailable: Boolean,
    @SerializedName("is_takeout_available") val isTakeoutAvailable: Boolean,
    @SerializedName("shop_minimum_order_amount") val shopMinimumOrderAmount: Int,
    @SerializedName("items") val items: List<CartItemResponse>,
    @SerializedName("items_amount") val itemsAmount: Int,
    @SerializedName("delivery_fee") val deliveryFee: Int,
    @SerializedName("total_amount") val totalAmount: Int,
    @SerializedName("final_payment_amount") val finalPaymentAmount: Int
) {
    data class CartItemResponse(
        @SerializedName("cart_menu_item_id") val cartMenuItemId: Int,
        @SerializedName("orderable_shop_menu_id") val orderableShopMenuId: Int,
        @SerializedName("name") val name: String,
        @SerializedName("menu_thumbnail_image_url") val menuThumbnailImageUrl: String,
        @SerializedName("quantity") val quantity: Int,
        @SerializedName("total_amount") val totalAmount: Int,
        @SerializedName("price") val price: CartPriceResponse,
        @SerializedName("options") val options: List<CartOptionResponse>,
        @SerializedName("is_modified") val isModified: Boolean
    ) {
        data class CartPriceResponse(
            @SerializedName("name") val name: String?,
            @SerializedName("price") val price: Int
        )

        data class CartOptionResponse(
            @SerializedName("option_group_name") val optionGroupName: String,
            @SerializedName("option_name") val optionName: String,
            @SerializedName("option_price") val optionPrice: Int
        )
    }
}
