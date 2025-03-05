package `in`.koreatech.business.feature.storemenu.managemenu.navigator

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.storemenu.managemenu.managemenu.ManageMenuScreen
import `in`.koreatech.business.navigation.MODIFYMENUSCREEN
import `in`.koreatech.business.navigation.toNavigateRegisterMenuScreen
import `in`.koreatech.business.navigation.toNavigateScreenWithMenuId

fun NavGraphBuilder.manageMenuScreen(navController: NavHostController) {
    navigation(
        route = "$MANAGEMENUSCREEN/{menuId}",
        startDestination = "${ManageMenuRoute.MODIFY_MENU.name}/{menuId}",
    ) {
        composable(
            route = "${ManageMenuRoute.MODIFY_MENU.name}/{menuId}",
            arguments =
                listOf(
                    navArgument("menuId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                ),
        ) {
            ManageMenuScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                navigateToModifyMenuScreen = { menuId ->
                    navController.toNavigateScreenWithMenuId(MODIFYMENUSCREEN, menuId)
                },
                navigateToRegisterMenuScreen = { storeId ->
                    navController.toNavigateRegisterMenuScreen(storeId)
                },
            )
        }
    }
}
