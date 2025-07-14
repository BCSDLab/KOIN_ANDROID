package `in`.koreatech.koin.domain.model.store

data class Shop(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val isDeliveryAvailable: Boolean,
    val isTakeoutAvailable: Boolean,
    val minimumOrderAmount: Int,
    val ratingAverage: Int,
    val reviewCount: Int,
    val minimumDeliveryTip: Int,
    val maximumDeliveryTip: Int,
    val isOpen: Boolean,
    val categoryIds: List<Int>,
    val imageUrls: List<String>,
    val open: List<OrderStoreShopsOpen>,
    val openStatus: String
) {
    data class OrderStoreShopsOpen(
        val dayOfWeek: Int,
        val closed: Boolean,
        val openTime: String,
        val closeTime: String
    )
}
