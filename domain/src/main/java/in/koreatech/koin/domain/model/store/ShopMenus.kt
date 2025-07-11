package `in`.koreatech.koin.domain.model.store

data class ShopMenus(
    val menuGroupId: Int,
    val menuGroupName: String,
    val menus: List<ShopMenu>
) {
    data class ShopMenu(
        val id: Int,
        val name: String,
        val description: String,
        val thumbnailImage: String,
        val isSoldOut: Boolean,
        val prices: List<ShopMenuPrice>
    ) {
        data class ShopMenuPrice(
            val id: Int,
            val name: String?,
            val price: Int
        )
    }
}
