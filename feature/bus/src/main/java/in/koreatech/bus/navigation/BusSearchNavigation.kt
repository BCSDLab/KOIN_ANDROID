package `in`.koreatech.bus.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.bus.screen.search.composable.BusSearchResultScreen
import `in`.koreatech.bus.screen.search.composable.BusSearchScreen

@Composable
fun BusSearchNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.BusSearch,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {

        composable<Routes.BusSearch> {
            BusSearchScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = { navController.popBackStack() },
                onSearch = { departure, arrival ->
                    navController.navigate(Routes.BusSearchResult(departure, arrival))
                }
            )
        }

        composable<Routes.BusSearchResult> {
            BusSearchResultScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = { navController.popBackStack() }

            )
        }
    }
}