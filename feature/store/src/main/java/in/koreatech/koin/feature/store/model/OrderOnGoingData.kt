package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.feature.store.enums.TypeOption

data class OrderOnGoingData(
    val status: TypeOption,
    val time: String,
    val storeImageUrl: String,
    val storeName: String,
    val orders: String,
    val price: Int
)
