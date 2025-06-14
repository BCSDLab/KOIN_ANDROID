package `in`.koreatech.bus.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.bus.animation.defaultEnterTransition
import `in`.koreatech.bus.animation.defaultExitTransition
import `in`.koreatech.bus.animation.defaultOutsideColor
import `in`.koreatech.bus.animation.defaultPopEnterTransition
import `in`.koreatech.bus.animation.defaultPopExitTransition
import `in`.koreatech.bus.screen.shuttle_timetable.composable.ShuttleTimetableScreen
import `in`.koreatech.bus.screen.timetable.composable.BusTimetableScreen
import `in`.koreatech.bus.util.findActivity

@Composable
fun BusTimetableNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    NavHost(
        modifier = modifier.background(defaultOutsideColor),
        navController = navController,
        startDestination = Routes.BusTimetable,
        enterTransition = {
            defaultEnterTransition()
        },
        exitTransition = {
            defaultExitTransition()
        },
        popEnterTransition = {
            defaultPopEnterTransition()
        },
        popExitTransition = {
            defaultPopExitTransition()
        }
    ) {
        composable<Routes.BusTimetable> {
            BusTimetableScreen(
                modifier = Modifier.fillMaxSize().background(Color.White),
                onNavigationIconClick = {
                    context.findActivity()?.finish()
                },
                onNavigateToShuttleTimetableScreen = {
                    navController.navigate(Routes.ShuttleTimetable(it.id))
                }
            )
        }

        composable<Routes.ShuttleTimetable> {
            ShuttleTimetableScreen(
                modifier = Modifier.fillMaxSize().background(Color.White),
                onNavigationIconClick = navController::popBackStack
            )
        }
    }
}
