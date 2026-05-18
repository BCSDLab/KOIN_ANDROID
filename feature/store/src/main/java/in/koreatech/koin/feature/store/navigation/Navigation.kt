package `in`.koreatech.koin.feature.store.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.store.BuildConfig
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_ADD_CART
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_DETAIL_MAIN
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_HOME
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_NEARBY
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_REVIEW
import `in`.koreatech.koin.feature.store.cart.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.cartadd.CartAddScreen
import `in`.koreatech.koin.feature.store.cartedit.CartEditScreen
import `in`.koreatech.koin.feature.store.detail.StoreDetailScreen
import `in`.koreatech.koin.feature.store.home.StoreHomeScreen
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.nearby.StoreNearbyScreen
import `in`.koreatech.koin.feature.store.orderhistory.OrderHistoryScreen
import `in`.koreatech.koin.feature.store.origin.ShopOriginInfoScreen
import `in`.koreatech.koin.feature.store.review.ReviewScreen
import `in`.koreatech.koin.feature.store.reviewadd.ReviewAddScreen
import `in`.koreatech.koin.feature.store.reviewedit.ReviewEditScreen
import `in`.koreatech.koin.feature.store.reviewreport.ReviewReportScreen
import `in`.koreatech.koin.feature.store.search.StoreSearchScreen
import `in`.koreatech.koin.feature.store.webapp.StoreWebAppScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.koinStoreGraph(
    navController: NavController,
    categoryId: Int,
    enableDelivery: Boolean,
    finish: () -> Unit = { }
) {
    navigation<StoreNavType.StoreMain>(
        startDestination = if (enableDelivery) StoreMainNavType.StoreMainHome::class else StoreMainNavType.StoreMainNearby::class
    ) {
        koinStoreMainGraph(
            navController = navController,
            categoryId = categoryId,
            finish = finish
        )
    }

    navigation<StoreNavType.StoreDetail>(
        startDestination = StoreDetailNavType.StoreDetailMain::class
    ) {
        koinStoreDetailGraph(
            navController = navController,
            finish = finish
        )
    }

    navigation<StoreNavType.StoreReview>(
        startDestination = StoreReviewNavType.StoreReviewHome::class,
        typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
    ) {
        koinStoreReviewGraph(
            navController = navController
        )
    }

    koinStoreCartScreens(navController = navController, finish = finish)
    koinStoreSearchPaymentScreens(navController = navController, finish = finish)
}

private fun NavGraphBuilder.koinStoreCartScreens(
    navController: NavController,
    finish: () -> Unit = { }
) {
    composable<StoreNavType.StoreCart> { entry ->
        val isCartModified by entry.savedStateHandle.getStateFlow(IS_CART_MODIFIED, initialValue = false).collectAsStateWithLifecycle()

        ShoppingCartScreen(
            isCartModified = isCartModified,
            navigateToStoreDetail = { storeId ->
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = true)) {
                    launchSingleTop = true
                    popUpTo(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = true)) {
                        inclusive = false
                    }
                }
                navController.currentBackStackEntry?.savedStateHandle?.set(IS_CART_MODIFIED, false)
            },
            navigateToPayment = {
                navController.navigate(StoreNavType.StorePayment(cartType = it))
            },
            navigateToCartEdit = {
                navController.navigate(StoreNavType.StoreCartEdit(cartMenuItemId = it))
            },
            navigateToStoreMain = {
                navController.previousBackStackEntry?.savedStateHandle?.set(IS_CART_MODIFIED, false)
                navController.navigate(StoreNavType.StoreMain)
            },
            navigateBack = {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        )
    }

    composable<StoreNavType.StoreCartAdd>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$DEEPLINK_STORE_ADD_CART/{$ORDERABLE_SHOP_ID}/{$ORDERABLE_SHOP_MENU_ID}/{$CART_DATA}"
            }
        )
    ) {
        CartAddScreen(
            navigateToCart = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CART_ADDED,
                    true
                )
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateBack = { isItemAdded ->
                if (isItemAdded) {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        IS_CART_ADDED,
                        true
                    )
                }

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CART_MODIFIED,
                    true
                )
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        )
    }

    composable<StoreNavType.StoreCartEdit> {
        CartEditScreen(
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateBack = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CART_MODIFIED,
                    true
                )
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        )
    }
}

private fun NavGraphBuilder.koinStoreSearchPaymentScreens(
    navController: NavController,
    finish: () -> Unit = { }
) {
    composable<StoreNavType.StoreSearch> { entry ->
        val args = entry.toRoute<StoreNavType.StoreSearch>()
        StoreSearchScreen(
            navigateToDetail = {
                navController.popBackStack()
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = it, isOrderableShop = args.isOrderableShop))
            },
            onBackPressed = {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        )
    }

    composable<StoreNavType.StorePayment> { entry ->
        val args = entry.toRoute<StoreNavType.StorePayment>()
        val url = "${BuildConfig.ORDER_BASE_URL}/payment?orderType=${args.cartType}"
        StoreWebAppScreen(
            url = url,
            finish = finish,
            navigateToMain = {
                navController.navigate(StoreNavType.StoreMain) {
                    popUpTo<StoreNavType.StoreMain> {
                        inclusive = true
                    }
                }
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart) {
                    popUpTo<StoreNavType.StoreCart> {
                        inclusive = true
                    }
                }
            },
            navigateToDetail = { storeId ->
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = true))
            }
        )
    }

    composable<StoreNavType.StoreOrderResult> { entry ->
        val args = entry.toRoute<StoreNavType.StoreOrderResult>()
        val url = "${BuildConfig.ORDER_BASE_URL}/result/${args.orderId}"

        StoreWebAppScreen(
            url = url,
            finish = finish,
            navigateToMain = {
                navController.navigate(StoreNavType.StoreMain) {
                    popUpTo<StoreNavType.StoreMain> {
                        inclusive = true
                    }
                }
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart) {
                    popUpTo<StoreNavType.StoreCart> {
                        inclusive = true
                    }
                }
            },
            navigateToDetail = { storeId ->
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = true))
            }
        )
    }
}

internal fun NavGraphBuilder.koinStoreMainGraph(
    navController: NavController,
    categoryId: Int,
    finish: () -> Unit = { }
) {
    composable<StoreMainNavType.StoreMainHome>(
        deepLinks = listOf(
            navDeepLink<StoreMainNavType.StoreMainHome>(basePath = DEEPLINK_STORE_MAIN_HOME)
        )
    ) {
        StoreHomeScreen(
            categoryId = categoryId,
            navigateToDetail = { storeId ->
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = true))
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateToSearch = {
                navController.navigate(StoreNavType.StoreSearch(isOrderableShop = true))
            },
            navigateToOrderResult = { orderId ->
                navController.navigate(StoreNavType.StoreOrderResult(orderId = orderId))
            }
        ) {
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }

    composable<StoreMainNavType.StoreMainNearby>(
        deepLinks = listOf(
            navDeepLink<StoreMainNavType.StoreMainNearby>(basePath = DEEPLINK_STORE_MAIN_NEARBY)
        )
    ) {
        StoreNearbyScreen(
            categoryId = categoryId,
            navigateToDetail = { storeId ->
                navController.navigate(StoreDetailNavType.StoreDetailMain(storeId = storeId, isOrderableShop = false))
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateToSearch = {
                navController.navigate(StoreNavType.StoreSearch(isOrderableShop = false))
            }
        ) {
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }

    composable<StoreMainNavType.StoreMainOrder> {
        OrderHistoryScreen(
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateToOrderResult = { orderId ->
                navController.navigate(StoreNavType.StoreOrderResult(orderId = orderId))
            }
        ) {
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }
}

@Suppress("LongMethod")
internal fun NavGraphBuilder.koinStoreDetailGraph(
    navController: NavController,
    finish: () -> Unit = { }
) {
    composable<StoreDetailNavType.StoreDetailMain>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$DEEPLINK_STORE_DETAIL_MAIN/{$STORE_ID}/{$IS_ORDERABLE_SHOP}"
            }
        )
    ) { entry ->
        val args = entry.toRoute<StoreDetailNavType.StoreDetailMain>()
        val isCartAdded by entry.savedStateHandle.getStateFlow(IS_CART_ADDED, initialValue = false).collectAsStateWithLifecycle()
        val isCartModified by entry.savedStateHandle.getStateFlow(IS_CART_MODIFIED, initialValue = false).collectAsStateWithLifecycle()

        StoreDetailScreen(
            isCartAdded = isCartAdded,
            isCartModified = isCartModified,
            navigateToBack = {
                if (!navController.popBackStack()) {
                    finish()
                }
            },
            navigateToCart = {
                entry.savedStateHandle[IS_CART_ADDED] = false
                navController.navigate(StoreNavType.StoreCart)
            },
            navigateToDetailInfo = { selectedInfoType ->
                navController.navigate(StoreDetailNavType.StoreDetailInfo(storeId = args.storeId, isOrderableShop = args.isOrderableShop, selectedInfo = selectedInfoType))
            },
            navigateToNotice = { storeId ->
                navController.navigate(StoreDetailNavType.StoreNotice(storeId = storeId))
            },
            navigateToReview = { storeNavigationData, storeName ->
                navController.navigate(
                    StoreReviewNavType.StoreReviewHome(
                        storeNavigationData = storeNavigationData,
                        storeName = storeName
                    )
                )
            },
            navigateToMenuInfo = { menuId ->
                entry.savedStateHandle[IS_CART_ADDED] = false
                navController.navigate(StoreNavType.StoreCartAdd(orderableShopId = args.storeId, orderableShopMenuId = menuId))
            }
        )
    }

    composable<StoreDetailNavType.StoreDetailInfo> { entry ->
        val args = entry.toRoute<StoreDetailNavType.StoreDetailInfo>()
        ShopOriginInfoScreen(
            selectedInfo = args.selectedInfo,
            onBackClick = {
                if (!navController.popBackStack()) {
                    finish()
                }
            },
            navigateToShoppingCart = {
                navController.navigate(StoreNavType.StoreCart)
            }
        )
    }

    composable<StoreDetailNavType.StoreNotice> {
    }
}

internal fun NavGraphBuilder.koinStoreReviewGraph(
    navController: NavController
) {
    composable<StoreReviewNavType.StoreReviewHome>(
        typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType),
        deepLinks = listOf(
            navDeepLink<StoreReviewNavType.StoreReviewHome>(
                basePath = DEEPLINK_STORE_REVIEW,
                typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
            )
        )
    ) {
        val isReviewUpdated by it.savedStateHandle.getStateFlow(IS_REVIEW_UPDATED, initialValue = false).collectAsStateWithLifecycle()
        ReviewScreen(
            isReviewUpdated = isReviewUpdated,
            onResetReviewUpdated = {
                navController.currentBackStackEntry?.savedStateHandle?.set(IS_REVIEW_UPDATED, false)
            },
            onReportClicked = { storeNavigationData, reviewId ->
                navController.navigate(StoreReviewNavType.StoreReviewReport(storeNavigationData, reviewId))
            },
            onAddReviewClicked = { storeNavigationData, storeName ->
                navController.navigate(StoreReviewNavType.StoreReviewAdd(storeNavigationData, storeName))
            },
            onEditReviewClicked = { storeNavigationData, reviewId, storeName ->
                navController.navigate(StoreReviewNavType.StoreReviewEdit(storeNavigationData, reviewId, storeName))
            }
        )
    }

    composable<StoreReviewNavType.StoreReviewAdd>(
        typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
    ) {
        ReviewAddScreen(
            onNavigateBack = { navController.popBackStack() },
            onReviewUpdated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(IS_REVIEW_UPDATED, true)
            }
        )
    }

    composable<StoreReviewNavType.StoreReviewEdit>(
        typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
    ) {
        ReviewEditScreen(
            onNavigateBack = { navController.popBackStack() },
            onReviewUpdated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(IS_REVIEW_UPDATED, true)
            }
        )
    }

    composable<StoreReviewNavType.StoreReviewReport>(
        typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
    ) {
        ReviewReportScreen()
    }
}

const val ORDERABLE_SHOP_MENU_ID = "orderableShopMenuId"
const val ORDERABLE_SHOP_ID = "orderableShopId"
const val STORE_ID = "storeId"
const val IS_ORDERABLE_SHOP = "isOrderableShop"
const val CART_MENU_ITEM_ID = "cartMenuItemId"
const val IS_CART_ADDED = "isCartAdded"
const val IS_CART_MODIFIED = "isCartModified"
const val CART_TYPE = "cartType"
const val SELECTED_INFO = "selectedInfo"
const val CART_DATA = "cartData"
const val ORDER_ID = "orderId"
const val IS_REVIEW_UPDATED = "isReviewUpdated"
