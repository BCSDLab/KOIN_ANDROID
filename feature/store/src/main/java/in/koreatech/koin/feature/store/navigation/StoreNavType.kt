package `in`.koreatech.koin.feature.store.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.StoreDetailInfoType
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import kotlinx.serialization.Serializable

@Serializable
sealed class StoreNavType {
    @Serializable
    data object StoreMain : StoreNavType()

    @Serializable
    data object StoreDetail : StoreNavType()

    @Serializable
    data object StoreReview : StoreNavType()

    @Serializable
    data object StoreCart : StoreNavType()

    @Serializable
    data class StoreCartAdd(
        val orderableShopId: Int,
        val orderableShopMenuId: Int,
        val cartData: String? = null
    ) : StoreNavType()

    @Serializable
    data class StoreCartEdit(val cartMenuItemId: Int) : StoreNavType()

    @Serializable
    data class StorePayment(val cartType: String) : StoreNavType()

    @Serializable
    data class StoreOrderResult(val orderId: Int) : StoreNavType()

    @Serializable
    data class StoreSearch(val isOrderableShop: Boolean) : StoreNavType()
}

@Serializable
sealed class StoreMainNavType {
    @Serializable
    data object StoreMainHome : StoreMainNavType()

    @Serializable
    data object StoreMainNearby : StoreMainNavType()

    @Serializable
    data object StoreMainOrder : StoreMainNavType()
}

@Serializable
sealed class StoreDetailNavType {
    @Serializable
    data class StoreDetailMain(
        val storeId: Int,
        val isOrderableShop: Boolean = true
    ) : StoreDetailNavType()

    @Serializable
    data class StoreDetailInfo(
        val storeId: Int,
        val isOrderableShop: Boolean = true,
        val selectedInfo: String = StoreDetailInfoType.ORIGIN.routeName
    ) : StoreDetailNavType()

    @Serializable
    data class StoreNotice(val storeId: Int) : StoreDetailNavType()
}

@Serializable
sealed class StoreReviewNavType {
    @Serializable
    data class StoreReviewHome(val storeNavigationData: StoreNavigationData, val storeName: String) : StoreReviewNavType()

    @Serializable
    data class StoreReviewAdd(val storeNavigationData: StoreNavigationData, val storeName: String) : StoreReviewNavType()

    @Serializable
    data class StoreReviewEdit(val storeNavigationData: StoreNavigationData, val reviewId: Int, val storeName: String) : StoreReviewNavType()

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
