package `in`.koreatech.koin.domain.model.store

data class CartItem(
    val orderableShopMenuPriceId: Int,
    val options: List<CartItemOption>
) {
    data class CartItemOption(
        val optionGroupId: Int,
        val optionId: Int
    )
}
