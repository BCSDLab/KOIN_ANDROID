package `in`.koreatech.koin.data.response.cart

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.cart.Cart

data class CartResponse(
    @SerializedName("shop_name")
    val shopName: String?,
    @SerializedName("shop_thumbnail_image_url")
    val shopThumbnailImageUrl: String?,
    @SerializedName("orderable_shop_id")
    val orderableShopId: Int?,
    @SerializedName("is_delivery_available")
    val isDeliveryAvailable: Boolean,
    @SerializedName("is_takeout_available")
    val isTakeoutAvailable: Boolean,
    @SerializedName("shop_minimum_order_amount")
    val shopMinimumOrderAmount: Int,
    @SerializedName("items")
    val items: List<CartItemResponse>,
    @SerializedName("items_amount")
    val itemsAmount: Int,
    @SerializedName("delivery_fee")
    val deliveryFee: Int,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("final_payment_amount")
    val finalPaymentAmount: Int
){
    fun toCart() =
        Cart(
            shopName = shopName ?: "",
            shopThumbnailImageUrl = shopThumbnailImageUrl ?: "",
            orderableShopId = orderableShopId ?: 0,
            isDeliveryAvailable = isDeliveryAvailable,
            isTakeoutAvailable = isTakeoutAvailable,
            shopMinimumOrderAmount = shopMinimumOrderAmount,
            items = items.map { it.toCartItem() },
            itemsAmount = itemsAmount,
            deliveryFee = deliveryFee,
            totalAmount = totalAmount,
            finalPaymentAmount = finalPaymentAmount
        )
}
