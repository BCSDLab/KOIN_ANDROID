package `in`.koreatech.koin.data.response.cart

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.cart.CartItem

data class CartItemResponse(
    @SerializedName("cart_menu_item_id")
    val cartMenuItemId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("menu_thumbnail_image_url")
    val menuThumbnailImageUrl: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("price")
    val price: CartItemPriceResponse,
    @SerializedName("options")
    val options: List<CartItemOptionResponse>,
    @SerializedName("is_modified")
    val isModified: Boolean
){
    fun toCartItem() =
        CartItem(
            cartMenuItemId = cartMenuItemId,
            name = name,
            menuThumbnailImageUrl = menuThumbnailImageUrl,
            quantity = quantity,
            totalAmount = totalAmount,
            price = price.toCartItemPrice(),
            options = options.map { it.toCartItemOption() },
            isModified = isModified
        )
}