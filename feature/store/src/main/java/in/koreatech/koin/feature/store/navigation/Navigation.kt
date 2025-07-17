package `in`.koreatech.koin.feature.store.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
        route = StoreNavType.StoreDetail.route,
        startDestination = StoreDetailNavType.StoreDetailMain.route
    ) {
        koinStoreDetailGraph(
            navController = navController,
            finish = finish
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
        route = StoreDetailNavType.StoreDetailMain.route
    ) {
    }

    composable(
        route = StoreDetailNavType.StoreDetailInfo.route
    ) {
    }
}
