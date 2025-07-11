package `in`.koreatech.koin.domain.model.store

data class ShopSummary(
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
    val images: List<ShopSummaryImage>
) {
    data class ShopSummaryImage(
        val imageUrl: String,
        val isThumbnail: Boolean
    )
}
