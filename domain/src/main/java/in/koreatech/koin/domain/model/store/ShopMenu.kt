package `in`.koreatech.koin.domain.model.store

data class ShopMenu(
    val id: Int,
    val name: String,
    val description: String,
    val images: List<String>,
    val isSoldOut: Boolean,
    val prices: List<ShopMenuPrice>,
    val optionGroups: List<ShopMenuOptionGroup>
) {
    data class ShopMenuPrice(
        val id: Int,
        val name: String?,
        val price: Int
    )

    data class ShopMenuOptionGroup(
        val id: Int,
        val name: String,
        val description: String,
        val isRequired: Boolean,
        val minSelect: Int,
        val maxSelect: Int,
        val options: List<ShopMenuPrice>
    )
}
