package `in`.koreatech.koin.domain.model.store

data class OrderOnGoingRelated(
    val id: Int,
    val paymentId: Int,
    val orderType: String,
    val orderableShopName: String,
    val orderableShopThumbnail: String,
    val estimatedAt: String,
    val orderStatus: String,
    val orderTitle: String,
    val totalAmount: Int
)
