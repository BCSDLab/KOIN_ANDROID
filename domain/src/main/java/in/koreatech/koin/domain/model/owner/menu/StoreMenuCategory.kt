package `in`.koreatech.koin.domain.model.owner.menu

data class StoreMenuCategory(
    val menuCategoryId: Int,
    val menuCategoryName: String,
    val menuCategoryIsChecked: Boolean = false
)