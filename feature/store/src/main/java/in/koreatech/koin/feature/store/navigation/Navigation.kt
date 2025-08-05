package `in`.koreatech.koin.feature.store.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.store.view.ShopOriginInfoScreen
import `in`.koreatech.koin.feature.store.view.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.view.StoreDetailScreen
import `in`.koreatech.koin.feature.store.view.cart.add.CartAddScreen
import `in`.koreatech.koin.feature.store.view.cart.edit.CartEditScreen
import `in`.koreatech.koin.feature.store.view.main.home.StoreHomeScreen
import `in`.koreatech.koin.feature.store.view.main.nearby.StoreNearbyScreen
import `in`.koreatech.koin.feature.store.view.payment.StorePaymentScreen
import `in`.koreatech.koin.feature.store.view.search.StoreSearchScreen

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
        route = "${StoreNavType.StoreDetail.route}/{$STORE_ID}",
        startDestination = "${StoreDetailNavType.StoreDetailMain.route}/{$STORE_ID}",
        arguments = listOf(
            navArgument(STORE_ID) {
                type = NavType.IntType
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
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$storeId/${true}")
            },
            navigateToPayment = {
                navController.navigate(StoreNavType.StorePayment.route)
            },
            navigateToCartEdit = {
                navController.navigate("${StoreNavType.StoreCartEdit.route}/$it")
            },
            navigateToStoreMain = {
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
        route = StoreNavType.StorePayment.route
    ) {
        StorePaymentScreen(
            finish = finish,
            navigateBack = {
                navController.navigate(StoreNavType.StoreMain.route) {
                    popUpTo(StoreNavType.StoreMain.route) {
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
        route = StoreMainNavType.StoreMainHome.route
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
            }
        ) {
            if (!navController.navigateUp()) {
                finish()
            }
        }
    }

    composable(
        route = StoreMainNavType.StoreMainNearby.route
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
            navigateToDetailInfo = {
                it.savedStateHandle[IS_CART_ADDED] = false
                navController.navigate("${StoreDetailNavType.StoreDetailInfo.route}/$storeId/$isOrderableShop")
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
        route = "${StoreDetailNavType.StoreDetailInfo.route}/{$STORE_ID}/{$IS_ORDERABLE_SHOP}",
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
        ShopOriginInfoScreen(
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
