package `in`.koreatech.koin.feature.store.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

sealed class StoreNavType(val route: String) {
    data object StoreMain : StoreNavType("store_main")
    data object StoreSearch : StoreNavType("store_search")
    data object StoreDetail : StoreNavType("store_detail")
    data object StoreReview : StoreNavType("store_review")
    data object StoreCart : StoreNavType("store_cart")
    data object StoreCartAdd : StoreDetailNavType("store_cart_add")
    data object StoreCartEdit : StoreDetailNavType("store_cart_edit")
    data object StorePayment : StoreNavType("store_payment")
    data object StoreOrderResult : StoreNavType("store_order_result")
}

sealed class StoreMainNavType(val route: String) {
    data object StoreMainHome : StoreMainNavType("store_main_home")
    data object StoreMainNearby : StoreMainNavType("store_main_nearby")
    data object StoreMainOrder : StoreMainNavType("store_main_order_history")
}

sealed class StoreDetailNavType(val route: String) {
    data object StoreDetailMain : StoreDetailNavType("store_detail_main")
    data object StoreDetailInfo : StoreDetailNavType("store_detail_info")
}

sealed class StoreReviewNavType(val route: String) {
    data object StoreReviewHome : StoreReviewNavType("store_review_home")
    data object StoreReviewAdd : StoreReviewNavType("store_review_add")
    data object StoreReviewEdit : StoreReviewNavType("store_review_edit")
    data object StoreReviewReport : StoreReviewNavType("store_review_report")
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
        type = StoreMainNavType.StoreMainOrder,
        iconRes = R.drawable.ic_store_navigation_order_history,
        stringRes = R.string.store_navigation_bar_order_history
    )
)
