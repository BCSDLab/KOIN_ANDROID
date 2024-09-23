package `in`.koreatech.business.feature.storemenu.modifymenu.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuCheckScreen
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuScreen
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuViewModel
import `in`.koreatech.business.navigation.MODIFYMENUSCREEN
import `in`.koreatech.business.navigation.sharedHiltViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.modifyMenuScreen(
    navController: NavHostController
){
    navigation(
        route = "${MODIFYMENUSCREEN}/{menuId}",
        startDestination = "${ModifyMenuRoute.MODIFY_MENU.name}/{menuId}",
    ){
        composable(
            route = "${ModifyMenuRoute.MODIFY_MENU.name}/{menuId}",
            arguments = listOf(
                navArgument("menuId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
                val viewModel: ModifyMenuViewModel = it.sharedHiltViewModel(navController =  navController)
                ModifyMenuScreen(
                    viewModel = viewModel,
                    onBackPressed = {
                        navController.navigateUp()
                    },
                    goToCheckMenuScreen = {
                        navController.navigate(ModifyMenuRoute.CHECK_MODIFY_MENU.name)
                    },
                )
        }

        composable(
            route = ModifyMenuRoute.CHECK_MODIFY_MENU.name
        ){
            val viewModel: ModifyMenuViewModel = it.sharedHiltViewModel(navController =  navController)
            ModifyMenuCheckScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }
}