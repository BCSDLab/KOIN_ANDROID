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
import `in`.koreatech.bus.screen.timetable.composable.BusTimetableScreen
import `in`.koreatech.bus.screen.shuttle_timetable.composable.ShuttleTimetableScreen

@Composable
fun BusTimetableNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.BusTimetable,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }
    ) {

        composable<Routes.BusTimetable> {
            BusTimetableScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = navController::popBackStack,
                onNavigateToShuttleTimetableDetailScreen = {
                    navController.navigate(Routes.ShuttleTimetableDetail(it.routeName, it.id))
                }
            )
        }

        composable<Routes.ShuttleTimetableDetail> {
            ShuttleTimetableScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = navController::popBackStack
            )
        }
    }
}