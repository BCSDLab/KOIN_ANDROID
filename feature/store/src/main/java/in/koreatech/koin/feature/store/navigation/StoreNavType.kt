package `in`.koreatech.koin.feature.store.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import kotlinx.serialization.Serializable

@Serializable
sealed class StoreNavType(val route: String) {
    data object StoreMain : StoreNavType("store_main")
    data object StoreSearch : StoreNavType("store_search")
    data object StoreDetail : StoreNavType("store_detail")

    @Serializable
    data object StoreReview : StoreNavType("store_review")
    data object StoreCart : StoreNavType("store_cart")
    data object StoreCartAdd : StoreNavType("store_cart_add")
    data object StoreCartEdit : StoreNavType("store_cart_edit")
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

@Serializable
sealed class StoreReviewNavType {
    @Serializable
    data class StoreReviewHome(val storeNavigationData: StoreNavigationData) : StoreReviewNavType()

    @Serializable
    data object StoreReviewAdd : StoreReviewNavType()

    @Serializable
    data object StoreReviewEdit : StoreReviewNavType()

    @Serializable
    data class StoreReviewReport(val storeNavigationData: StoreNavigationData, val reviewId: Int) : StoreReviewNavType()
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
