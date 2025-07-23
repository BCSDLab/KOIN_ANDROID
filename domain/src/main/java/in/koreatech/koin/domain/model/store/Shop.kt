package `in`.koreatech.koin.domain.model.store

data class Shop(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val isDeliveryAvailable: Boolean,
    val isTakeoutAvailable: Boolean,
    val serviceEvent: Boolean,
    val minimumOrderAmount: Int,
    val ratingAverage: Double,
    val reviewCount: Int,
    val minimumDeliveryTip: Int,
    val maximumDeliveryTip: Int,
    val isOpen: Boolean,
    val categoryIds: List<Int>,
    val images: List<ShopImageUrls>,
    val open: List<OrderStoreShopsOpen>,
    val openStatus: OpenStatus
) {
    data class OrderStoreShopsOpen(
        val dayOfWeek: Int,
        val closed: Boolean,
        val openTime: String,
        val closeTime: String
    )

    data class ShopImageUrls(
        val imageUrl: String,
        val isThumbnail: Boolean
    )
}
