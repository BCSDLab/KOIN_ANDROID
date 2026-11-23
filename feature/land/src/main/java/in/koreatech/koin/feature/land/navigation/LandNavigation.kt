package `in`.koreatech.koin.feature.land.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.koin.feature.land.model.LandUiModel
import `in`.koreatech.koin.feature.land.ui.detail.LandDetailScreen
import `in`.koreatech.koin.feature.land.ui.main.LandScreen
import `in`.koreatech.koin.feature.land.ui.search.LandSearchScreen

@Composable
internal fun LandNavigation(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigateToDetail: (LandUiModel) -> Unit = { land ->
        navController.navigate(
            LandNavType.LandDetail(
                landId = land.id,
                isMonthlyAvailable = land.isMonthlyAvailable,
                isCharterAvailable = land.isCharterAvailable
            )
        ) {
            launchSingleTop = true
        }
    }
    NavHost(
        navController = navController,
        startDestination = LandNavType.LandMain,
        modifier = modifier
    ) {
        composable<LandNavType.LandMain> {
            LandScreen(
                onNavigateToDetail = navigateToDetail,
                onSearchClick = {
                    navController.navigate(LandNavType.LandSearch) {
                        launchSingleTop = true
                    }
                },
                onNavigateBack = {
                    if (!navController.popBackStack()) {
                        onNavigateBack()
                    }
                }
            )
        }
        composable<LandNavType.LandSearch> {
            LandSearchScreen(
                onNavigateToDetail = navigateToDetail,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<LandNavType.LandDetail> {
            LandDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
