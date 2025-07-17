package `in`.koreatech.koin.feature.store.navigation

import androidx.compose.foundation.pager.rememberPagerState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.store.view.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.view.StoreDetailScreen
import `in`.koreatech.koin.feature.store.view.main.home.StoreHomeScreen

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
        ShoppingCartScreen {
            if (!navController.navigateUp()) {
                finish()
            }
        }
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
                navController.navigate("${StoreDetailNavType.StoreDetailMain.route}/$storeId")
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

    composable(
        route = StoreMainNavType.StoreMainNearby.route
    ) {
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
        route = "${StoreDetailNavType.StoreDetailMain.route}/{storeId}",
        arguments = listOf(
            navArgument("storeId") {
                type = NavType.IntType
            }
        )
    ) {
        val pagerState = rememberPagerState(0, 0f) { 1 }

        StoreDetailScreen(
            pagerState = pagerState,
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
            }
        )
    }

    composable(
        route = StoreDetailNavType.StoreDetailInfo.route
    ) {
    }
}
