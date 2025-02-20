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
            val storeId = it.arguments?.getInt("storeId") ?: -1

            WriteEventScreen(
                shopId = storeId,
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
