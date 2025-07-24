package `in`.koreatech.koin.domain.model.store

data class CartItem(
    val orderableShopMenuPriceId: Int,
    val options: List<AddCartItemOption>?
)
