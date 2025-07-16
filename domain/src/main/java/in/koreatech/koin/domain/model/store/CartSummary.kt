package `in`.koreatech.koin.domain.model.store

data class CartSummary(
    val orderableShopId: Int,
    val shopMinimumOrderAmount: Int,
    val cartItemsAmount: Int,
    val isAvailable: Boolean
)
