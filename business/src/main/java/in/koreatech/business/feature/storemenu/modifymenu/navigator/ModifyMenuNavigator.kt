package `in`.koreatech.business.feature.storemenu.modifymenu.navigator

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.storemenu.registermenu.navigator.RegisterMenuRoute
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuScreen
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModifyMenuNavigator(
    navController: NavHostController = rememberNavController(),
    modifyMenuViewModel: ModifyMenuViewModel = hiltViewModel(),
    menuId: Int = 2796 //Todo 네비게이션으로 바꿔서 파라미터로 안받도록 변경하기
) {

    LaunchedEffect(menuId) {
        navController.navigate("${ModifyMenuRoute.MODIFY_MENU.name}/$menuId")
    }

    NavHost(
        navController = navController,
        startDestination = "${ModifyMenuRoute.MODIFY_MENU.name}/{menuId}",
    ) {
        composable(
            route = "${ModifyMenuRoute.MODIFY_MENU.name}/{menuId}",
            arguments = listOf(
                navArgument("menuId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {backStackEntry ->
            val menuId = backStackEntry.arguments?.getInt("menuId") ?: -1

            if(menuId != -1){
                modifyMenuViewModel.settingId(menuId)

                ModifyMenuScreen(
                    viewModel = modifyMenuViewModel,
                    onBackPressed = {
                        navController.navigateUp()
                    },
                    goToCheckMenuScreen = {
                        navController.navigate(ModifyMenuRoute.CHECK_MODIFY_MENU.name)
                    },
                )
            }
        }
    }
}