package `in`.koreatech.koin.domain.model.store

data class CartAdd(
    val orderableShopId: Int,
    val orderableShopMenuId: Int,
    val orderableShopMenuPriceId: Int,
    val orderableShopMenuOptionIds: List<CartAddOption>
) {
    data class CartAddOption(
        val optionGroupId: Int,
        val optionId: Int
    )
}
