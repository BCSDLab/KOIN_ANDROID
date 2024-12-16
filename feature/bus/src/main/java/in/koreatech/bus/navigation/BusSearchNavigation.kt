package `in`.koreatech.bus.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.bus.animation.defaultEnterTransition
import `in`.koreatech.bus.animation.defaultExitTransition
import `in`.koreatech.bus.animation.defaultOutsideColor
import `in`.koreatech.bus.animation.defaultPopEnterTransition
import `in`.koreatech.bus.animation.defaultPopExitTransition
import `in`.koreatech.bus.screen.search.composable.BusSearchScreen
import `in`.koreatech.bus.screen.searchresult.composable.BusSearchResultScreen
import `in`.koreatech.bus.type.PlaceType
import kotlin.reflect.typeOf

@Composable
fun BusSearchNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        modifier = modifier.background(defaultOutsideColor),
        navController = navController,
        startDestination = Routes.BusSearch,
        enterTransition = {
            defaultEnterTransition()
        }, exitTransition = {
            defaultExitTransition()
        }, popEnterTransition = {
            defaultPopEnterTransition()
        }, popExitTransition = {
            defaultPopExitTransition()
        }
    ) {

        composable<Routes.BusSearch>(
            typeMap = mapOf(
                typeOf<PlaceType>() to PlaceTypeNavType
            )
        ) {
            BusSearchScreen(
                modifier = Modifier.fillMaxSize().background(Color.White),
                onNavigationIconClick = { navController.popBackStack() },
                onSearch = { departure, arrival ->
                    navController.navigate(Routes.BusSearchResult(departure, arrival))
                }
            )
        }

        composable<Routes.BusSearchResult> {
            BusSearchResultScreen(
                modifier = Modifier.fillMaxSize().background(Color.White),
                onNavigationIconClick = { navController.popBackStack() }
            )
        }
    }
}