package `in`.koreatech.koin.domain.model.ordershop

data class OrderShopSummary(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val isDeliveryAvailable: Boolean,
    val isTakeoutAvailable: Boolean,
    val payCard: Boolean,
    val payBank: Boolean,
    val minimumOrderAmount: Int,
    val ratingAverage: Float,
    val reviewCount: Int,
    val minimumDeliveryTip: Int,
    val maximumDeliveryTip: Int,
    val images: List<String>,
)
