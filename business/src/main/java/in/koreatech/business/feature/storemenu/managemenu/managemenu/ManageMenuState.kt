package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import kotlinx.collections.immutable.ImmutableList

data class ManageMenuState(
    val storeId: Int = -1,
    val storeMenuList: ImmutableList<StoreMenuCategories>? = null,
    val storeMenuCategoryList: MutableList<MenuCategory> =
        mutableListOf(
            MenuCategory("추천 메뉴", true),
            MenuCategory("메인 메뉴", false),
            MenuCategory("세트 메뉴", false),
            MenuCategory("사이드 메뉴", false),
        ),
    val isRecommendMenuChecked: Boolean = true,
    val isMainMenuChecked: Boolean = false,
    val isSetMenuChecked: Boolean = false,
    val isSideMenuChecked: Boolean = false,
    val currentCheckboxState: CheckBoxState = CheckBoxState.RECOMMENDMENU,
)

enum class CheckBoxState {
    RECOMMENDMENU,
    MAINMENU,
    SETMENU,
    SIDEMENU,
}
