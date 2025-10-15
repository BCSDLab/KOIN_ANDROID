package `in`.koreatech.koin.domain.model.store

data class CartItem(
    val quantity: Int,
    val orderableShopMenuPriceId: Int,
    val options: List<AddCartItemOption>?
)
