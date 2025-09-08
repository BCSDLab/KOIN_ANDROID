package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.enums.OrderStatus
import `in`.koreatech.koin.feature.store.enums.StoreStatus

data class OrderHistoryData(
    val id: Int,
    val paymentId: Int,
    val orderableShopId: Int,
    val orderableShopName: String,
    val orderDate: String,
    val orderStatus: OrderStatus,
    val orderTitle: String,
    // TODO 이건 뭐 ShopId같은걸 사용해서 가져와야 할거 같은데..?
    val storeStatus: StoreStatus,
    val storeImageUrl: String,
    val price: Int
)
