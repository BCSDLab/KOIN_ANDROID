package `in`.koreatech.koin.feature.store.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

sealed class StoreNavType(val route: String) {
    data object StoreMain : StoreNavType("store_home")
    data object StoreDetail : StoreNavType("store_detail")
}

sealed class StoreMainNavType(val route: String) {
    data object StoreMainHome : StoreMainNavType("store_main_home")
    data object StoreMainNearby : StoreMainNavType("store_main_nearby")
    data object StoreMainOrderHistory : StoreMainNavType("store_main_order_history")
}

sealed class StoreDetailNavType(val route: String) {
    data object StoreDetailMain : StoreDetailNavType("store_detail_main")
    data object StoreDetailInfo : StoreDetailNavType("store_detail_info")
}

data class NavigationBarItem(
    val type: StoreMainNavType,
    @DrawableRes val iconRes: Int,
    @StringRes val stringRes: Int
)

val navigationBarItems = listOf(
    NavigationBarItem(
        type = StoreMainNavType.StoreMainHome,
        iconRes = R.drawable.ic_store_navigation_home,
        stringRes = R.string.store_navigation_bar_home
    ),
    NavigationBarItem(
        type = StoreMainNavType.StoreMainNearby,
        iconRes = R.drawable.ic_store_navigation_nearby,
        stringRes = R.string.store_navigation_bar_nearby
    ),
    NavigationBarItem(
        type = StoreMainNavType.StoreMainOrderHistory,
        iconRes = R.drawable.ic_store_navigation_order_history,
        stringRes = R.string.store_navigation_bar_order_history
    )
)
