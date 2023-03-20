package `in`.koreatech.koin.domain.model.store

import java.time.LocalDateTime

data class StoreMenu(
    val id: Int,
    val shopId: Int,
    val name: String,
    val priceType: List<StoreMenuPrice>
)
