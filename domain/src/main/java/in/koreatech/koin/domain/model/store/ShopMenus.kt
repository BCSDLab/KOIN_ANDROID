package `in`.koreatech.koin.domain.model.store

data class ShopMenus(
    val id: Int,
    val name: String,
    val isHidden: Boolean,
    val isSingle: Boolean,
    val singlePrice: Int?,
    val optionPrices: List<ShopMenuOptions>?,
    val description: String?,
    val imageUrls: List<String>?,
) {
    data class ShopMenuOptions(
        val option: String,
        val price: Int
    )
}
