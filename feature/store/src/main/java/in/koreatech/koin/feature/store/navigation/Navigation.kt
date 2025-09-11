package `in`.koreatech.koin.feature.store.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.feature.store.BuildConfig
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_ADD_CART
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_DETAIL_MAIN
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_HOME
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_NEARBY
import `in`.koreatech.koin.feature.store.cart.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.cartadd.CartAddScreen
import `in`.koreatech.koin.feature.store.cartedit.CartEditScreen
import `in`.koreatech.koin.feature.store.detail.StoreDetailScreen
import `in`.koreatech.koin.feature.store.enums.StoreDetailInfoType
import `in`.koreatech.koin.feature.store.home.StoreHomeScreen
import `in`.koreatech.koin.feature.store.nearby.StoreNearbyScreen
import `in`.koreatech.koin.feature.store.origin.ShopOriginInfoScreen
import `in`.koreatech.koin.feature.store.search.StoreSearchScreen
import `in`.koreatech.koin.feature.store.webapp.StoreWebAppScreen
import `in`.koreatech.koin.feature.store.view.orders.OrderHistoryScreen

fun NavGraphBuilder.koinStoreGraph(
    navController: NavController,
    categoryId: Int,
    finish: () -> Unit = { }
) {
    navigation(
        route = StoreNavType.StoreMain.route,
        startDestination = StoreMainNavType.StoreMainHome.route
    ) {
        koinStoreMainGraph(
            navController = navController,
            categoryId = categoryId,
            finish = finish
        )
    }

    navigation(
        route = "${StoreNavType.StoreDetail.route}/{$STORE_ID}/{$IS_ORDERABLE_SHOP}",
        startDestination = "${StoreDetailNavType.StoreDetailMain.route}/{$STORE_ID}/{$IS_ORDERABLE_SHOP}",
        arguments = listOf(
            navArgument(STORE_ID) {
                type = NavType.IntType
            },
            navArgument(IS_ORDERABLE_SHOP) {
                type = NavType.BoolType
                defaultValue = true
            }
        )
    ) {
        koinStoreDetailGraph(
            navController = navController,
            finish = finish
        )
    }

    composable(
        route = StoreNavType.StoreCart.route
    ) {
        val isCartModified by it.savedStateHandle.getStateFlow(IS_CART_MODIFIED, initialValue = false).collectAsStateWithLifecycle()

        ShoppingCartScreen(
            isCartModified = isCartModified,
            navigateToStoreDetail = { storeId ->
                val targetRoute = "${StoreDetailNavType.StoreDetailMain.route}/$storeId/${true}"
                navController.navigate(targetRoute) {
                    launchSingleTop = true
                    popUpTo(targetRoute) {
                        inclusive = false
                    }
                }
                navController.currentBackStackEntry?.savedStateHandle?.set(IS_CART_MODIFIED, false)
            },
            navigateToPayment = {
                navController.navigate("${StoreNavType.StorePayment.route}/$it")
            },
            navigateToCartEdit = {
                navController.navigate("${StoreNavType.StoreCartEdit.route}/$it")
            },
            navigateToStoreMain = {
                navController.previousBackStackEntry?.savedStateHandle?.set(IS_CART_MODIFIED, false)
                navController.navigate(StoreNavType.StoreMain.route)
            },
            navigateBack = {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        )
    }

    composable(
        route = "${StoreNavType.StoreCartAdd.route}/{$ORDERABLE_SHOP_ID}/{$ORDERABLE_SHOP_MENU_ID}",
        arguments = listOf(
            navArgument(ORDERABLE_SHOP_ID) {
                type = NavType.IntType
            },
            navArgument(ORDERABLE_SHOP_MENU_ID) {
                type = NavType.IntType
            },
            navArgument(CART_DATA) {
                type = NavType.StringType
                nullable = true
            }
        ),
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
                navController.navigate(StoreNavType.StoreCart.route)
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
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        )
    }

    composable(
        route = "${StoreNavType.StoreCartEdit.route}/{$CART_MENU_ITEM_ID}",
        arguments = listOf(
            navArgument(CART_MENU_ITEM_ID) {
                type = NavType.IntType
            }
        )
    ) {
        CartEditScreen(
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateBack = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CART_MODIFIED,
                    true
                )
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        )
    }

    composable(
        route = StoreNavType.StoreSearch.route
    ) {
        StoreSearchScreen(
            navigateToDetail = {
                navController.navigateUp()
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$it/${true}")
            },
            onBackPressed = {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        )
    }

    composable(
        route = "${StoreNavType.StorePayment.route}/{$CART_TYPE}",
        arguments = listOf(
            navArgument(CART_TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        val cartType = it.arguments?.getString(CART_TYPE) ?: CartType.DELIVERY.name
        val url = "${BuildConfig.ORDER_BASE_URL}/payment?orderType=$cartType"
        StoreWebAppScreen(
            url = url,
            finish = finish,
            navigateToMain = {
                navController.navigate(StoreNavType.StoreMain.route) {
                    popUpTo(StoreNavType.StoreMain.route) {
                        inclusive = true
                    }
                }
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route) {
                    popUpTo(StoreNavType.StoreCart.route) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable(
        route = "${StoreNavType.StoreOrderResult.route}/{$ORDER_ID}",
        arguments = listOf(
            navArgument(ORDER_ID) {
                type = NavType.IntType
            }
        )
    ) {
        val orderId = it.arguments?.getInt(ORDER_ID)
        val url = "${BuildConfig.ORDER_BASE_URL}/result/$orderId"

        StoreWebAppScreen(
            url = url,
            finish = finish,
            navigateToMain = {
                navController.navigate(StoreNavType.StoreMain.route) {
                    popUpTo(StoreNavType.StoreMain.route) {
                        inclusive = true
                    }
                }
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route) {
                    popUpTo(StoreNavType.StoreCart.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

internal fun NavGraphBuilder.koinStoreMainGraph(
    navController: NavController,
    categoryId: Int,
    finish: () -> Unit = { }
) {
    composable(
        route = StoreMainNavType.StoreMainHome.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEPLINK_STORE_MAIN_HOME
            }
        )
    ) {
        StoreHomeScreen(
            categoryId = categoryId,
            navigateToDetail = { storeId ->
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$storeId/${true}")
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateToSearch = {
                navController.navigate(StoreNavType.StoreSearch.route)
            },
            navigateToOrderResult = { orderId ->
                navController.navigate("${StoreNavType.StoreOrderResult.route}/$orderId")
            }
        ) {
            if (!navController.navigateUp()) {
                finish()
            }
        }
    }

    composable(
        route = StoreMainNavType.StoreMainNearby.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEPLINK_STORE_MAIN_NEARBY
            }
        )
    ) {
        StoreNearbyScreen(
            navigateToDetail = { storeId ->
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$storeId/${false}")
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateToSearch = {
                navController.navigate(StoreNavType.StoreSearch.route)
            }
        ) {
            if (!navController.navigateUp()) {
                finish()
            }
        }
    }

    composable(
        route = StoreMainNavType.StoreMainOrderHistory.route
    ) {
        OrderHistoryScreen(
            navigateToDetail = { storeId ->
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$storeId/${false}")
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            }
        ) {
            if (!navController.navigateUp()) {
                finish()
            }
        }
    }
}

internal fun NavGraphBuilder.koinStoreDetailGraph(
    navController: NavController,
    finish: () -> Unit = { }
) {
    composable(
        route = "${StoreDetailNavType.StoreDetailMain.route}/{$STORE_ID}/{$IS_ORDERABLE_SHOP}",
        arguments = listOf(
            navArgument(STORE_ID) {
                type = NavType.IntType
            },
            navArgument(IS_ORDERABLE_SHOP) {
                type = NavType.BoolType
                defaultValue = true
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$DEEPLINK_STORE_DETAIL_MAIN/{$STORE_ID}/{$IS_ORDERABLE_SHOP}"
            }
        )
    ) {
        val isCartAdded by it.savedStateHandle.getStateFlow(IS_CART_ADDED, initialValue = false).collectAsStateWithLifecycle()
        val isCartModified by it.savedStateHandle.getStateFlow(IS_CART_MODIFIED, initialValue = false).collectAsStateWithLifecycle()
        val storeId = it.arguments?.getInt(STORE_ID) ?: 0
        val isOrderableShop = it.arguments?.getBoolean(IS_ORDERABLE_SHOP) ?: true
        StoreDetailScreen(
            isCartAdded = isCartAdded,
            isCartModified = isCartModified,
            navigateToBack = {
                if (!navController.navigateUp()) {
                    finish()
                }
            },
            navigateToCart = {
                it.savedStateHandle[IS_CART_ADDED] = false
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateToDetailInfo = { selectedInfoType ->
                navController.navigate("${StoreDetailNavType.StoreDetailInfo.route}/$storeId/$isOrderableShop/$selectedInfoType")
            },
            navigateToReview = {
                // Navigate to review screen if implemented
            },
            navigateToMenuInfo = { menuId ->
                it.savedStateHandle[IS_CART_ADDED] = false
                navController.navigate("${StoreNavType.StoreCartAdd.route}/$storeId/$menuId")
            }
        )
    }

    composable(
        route = "${StoreDetailNavType.StoreDetailInfo.route}/{$STORE_ID}/{$IS_ORDERABLE_SHOP}/{$SELECTED_INFO}",
        arguments = listOf(
            navArgument(STORE_ID) {
                type = NavType.IntType
            },
            navArgument(IS_ORDERABLE_SHOP) {
                type = NavType.BoolType
                defaultValue = true
            },
            navArgument(SELECTED_INFO) {
                type = NavType.StringType
                defaultValue = StoreDetailInfoType.ORIGIN.routeName
            }
        )
    ) {
        val selectedInfo = it.arguments?.getString(SELECTED_INFO) ?: StoreDetailInfoType.ORIGIN.routeName
        ShopOriginInfoScreen(
            selectedInfo = selectedInfo,
            onBackClick = {
                if (!navController.navigateUp()) {
                    finish()
                }
            },
            navigateToShoppingCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            }
        )
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
