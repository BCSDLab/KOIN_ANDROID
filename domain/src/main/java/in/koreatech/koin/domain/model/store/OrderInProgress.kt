package `in`.koreatech.koin.domain.model.store

data class OrderInProgress(
    val orderId: Int,
    val orderType: String,
    val shopName: String,
    val shopThumbnail: String,
    val estimatedAt: String?,
    val orderStatus: String,
    val paymentDescription: String,
    val totalAmount: Int
)
