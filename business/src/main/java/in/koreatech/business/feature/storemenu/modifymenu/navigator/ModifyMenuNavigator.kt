package `in`.koreatech.business.feature.storemenu.modifymenu.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.storemenu.registermenu.navigator.RegisterMenuRoute
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuScreen
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ModifyMenuViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModifyMenuNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    modifyMenuViewModel: ModifyMenuViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = ModifyMenuRoute.MODIFY_MENU.name,
        modifier = modifier
    ) {

        composable(
            route = ModifyMenuRoute.MODIFY_MENU.name,
        ) {
            ModifyMenuScreen(
                viewModel = modifyMenuViewModel,
                onBackPressed = {
                    navController.navigateUp()
                },
                goToCheckMenuScreen = {
                    navController.navigate(ModifyMenuRoute.CHECK_MODIFY_MENU.name)
                }
            )
        }
    }
}