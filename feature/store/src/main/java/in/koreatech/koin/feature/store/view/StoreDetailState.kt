package `in`.koreatech.koin.feature.store.view

import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreDetailScrollType
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

data class StoreDetailState(
    val store: StoreWithMenu = StoreWithMenu.empty(),
    val categories: List<MenuCategory> = emptyList(),
    val storeMenu: List<ShopMenus> = emptyList(),
    val storeReview: StoreReview = StoreReview.empty(),
    val scrollUp: StoreDetailScrollType = StoreDetailScrollType.NONE,
    val availableDelivery: Boolean = false,
    val isLogin: Boolean = false,
    val isLoading: Boolean = true
) {
    data class MenuCategory(
        val storeMenuCategories: StoreMenuCategories = StoreMenuCategories(
            id = 0,
            name = null,
            menus = emptyList()
        ),
        val isChecked: Boolean = false
    )
}
