package `in`.koreatech.koin.feature.store.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.store.view.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.view.StoreDetailScreen
import `in`.koreatech.koin.feature.store.view.main.home.StoreHomeScreen
import `in`.koreatech.koin.feature.store.view.main.nearby.StoreNearbyScreen
import `in`.koreatech.koin.feature.store.view.menu.AddMenuScreen
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
        route = "${StoreNavType.StoreDetail.route}/{storeId}",
        startDestination = "${StoreDetailNavType.StoreDetailMain.route}/{storeId}",
        arguments = listOf(
            navArgument("storeId") {
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
        ShoppingCartScreen(
            navigateToStoreDetail = {
                if (!navController.navigateUp()) {
                    finish()
                }
            },
            navigateToPayment = {
                navController.navigate(StoreNavType.StorePayment.route)
            }
        )
    }

    composable(
        route = StoreNavType.StoreSearch.route
    ) {
        StoreSearchScreen(
            navigateToDetail = {
                navController.navigateUp()
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$it")
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
                if (!navController.navigateUp()) {
                    finish()
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
        route = "${StoreDetailNavType.StoreDetailMain.route}/{storeId}/{isOrderableShop}",
        arguments = listOf(
            navArgument("storeId") {
                type = NavType.IntType
            },
            navArgument("isOrderableShop") {
                type = NavType.BoolType
                defaultValue = true
            }
        )
    ) {
        val storeId = it.arguments?.getInt("storeId") ?: 0
        StoreDetailScreen(
            navigateToBack = {
                if (!navController.navigateUp()) {
                    finish()
                }
            },
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateToDetailInfo = {
                navController.navigate(StoreDetailNavType.StoreDetailInfo.route)
            },
            navigateToReview = {
                // Navigate to review screen if implemented
            },
            navigateToMenuInfo = { menuId ->
                navController.navigate("${StoreDetailNavType.StoreDetailMenu.route}/$storeId/$menuId")
            }
        )
    }

    composable(
        route = StoreDetailNavType.StoreDetailInfo.route
    ) {
    }

    composable(
        route = "${StoreDetailNavType.StoreDetailMenu.route}/{$ORDERABLE_STORE_ID}/{$CART_MENU_ID}",
        arguments = listOf(
            navArgument(ORDERABLE_STORE_ID) {
                type = NavType.IntType
            },
            navArgument(CART_MENU_ID) {
                type = NavType.IntType
            }
        )
    ) {
        AddMenuScreen(
            navigateToCart = {
                navController.navigate(StoreNavType.StoreCart.route)
            },
            navigateToBack = {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        )
    }
}

const val CART_MENU_ID = "cartMenuId"
const val ORDERABLE_STORE_ID = "orderableStoreId"
const val KEY_ADD_MENU_MODE = "addMenuMode"
