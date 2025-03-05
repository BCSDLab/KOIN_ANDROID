package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

sealed class ManageMenuSideEffect {
    data class NavigateToModifyMenuScreen(val menuId: Int) : ManageMenuSideEffect()

    data class NavigateToRegisterMenuScreen(val storeId: Int) : ManageMenuSideEffect()
}
