package `in`.koreatech.koin.domain.model.cart

data class CartItem(
    val cartMenuItemId: Int,
    val name: String,
    val menuThumbnailImageUrl: String,
    val quantity: Int,
    val totalAmount: Int,
    val price: CartItemPrice,
    val options: List<CartItemOption>,
    val isModified: Boolean
)
