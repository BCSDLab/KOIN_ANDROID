package `in`.koreatech.koin.domain.model.cart

data class Cart(
    val shopName: String?,
    val shopThumbnailImageUrl: String?,
    val orderableShopId: Int?,
    val isDeliveryAvailable: Boolean,
    val isTakeoutAvailable: Boolean,
    val shopMinimumOrderAmount: Int,
    val items: List<CartItem>,
    val itemsAmount: Int,
    val deliveryFee: Int,
    val totalAmount: Int,
    val finalPaymentAmount: Int
) {
    companion object {
        val Empty = Cart(
            shopName = null,
            shopThumbnailImageUrl = null,
            orderableShopId = null,
            isDeliveryAvailable = false,
            isTakeoutAvailable = false,
            shopMinimumOrderAmount = 0,
            items = emptyList(),
            itemsAmount = 0,
            deliveryFee = 0,
            totalAmount = 0,
            finalPaymentAmount = 0
        )
    }
}
