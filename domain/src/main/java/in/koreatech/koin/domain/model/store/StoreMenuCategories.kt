package `in`.koreatech.koin.domain.model.store

data class StoreMenuCategories(
    val id: Int,
    val name: String?,
    val menus: List<ShopMenus>?
)
