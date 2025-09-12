package `in`.koreatech.koin.domain.model.store

data class OrderHistoryRelated(
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int,
    val orders: List<OrderHistoryOrders>
)

data class OrderHistoryOrders(
    val id: Int,
    val paymentId: Int,
    val orderableShopId: Int,
    val orderableShopName: String,
    val openStatus: Boolean,
    val orderableShopThumbnail: String,
    val orderDate: String,
    val orderStatus: String,
    val orderTitle: String,
    val totalAmount: Int
)
