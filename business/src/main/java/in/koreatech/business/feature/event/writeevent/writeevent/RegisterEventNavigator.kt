package `in`.koreatech.business.feature.event.writeevent.writeevent

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.business.navigation.ADDEVENT


fun NavGraphBuilder.registerEventScreen(
    navController: NavHostController
) {
    navigation(
        route = "${ADDEVENT}/{storeId}",
        startDestination = "${RegisterEventRoute.REGISTER_EVENT.name}/{storeId}",
    ) {
        composable(
            route = "${RegisterEventRoute.REGISTER_EVENT.name}/{storeId}",
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
            WriteEventScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                goToMyStoreScreen = {
                    navController.navigateUp()
                }
            )
        }

    }
}
