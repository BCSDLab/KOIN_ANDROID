package `in`.koreatech.business.feature.storemenu.registermenu.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import `in`.koreatech.business.feature.storemenu.registermenu.registermenu.RegisterMenuCheckScreen
import `in`.koreatech.business.feature.storemenu.registermenu.registermenu.RegisterMenuScreen
import `in`.koreatech.business.feature.storemenu.registermenu.registermenu.RegisterMenuViewModel
import `in`.koreatech.business.navigation.REGISTERMENUSCREEN
import `in`.koreatech.business.navigation.sharedHiltViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.registerMenu(
    navController: NavHostController
){
    navigation(
        route = REGISTERMENUSCREEN,
        startDestination = RegisterMenuRoute.REGISTER.name,
    ) {

        composable(
            route = RegisterMenuRoute.REGISTER.name,
        ) {
            val viewModel: RegisterMenuViewModel = it.sharedHiltViewModel(navController = navController)
            RegisterMenuScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                },
                goToCheckMenuScreen = {
                    navController.navigate(RegisterMenuRoute.CHECK_MENU.name)
                }
            )
        }

        composable(
            route = RegisterMenuRoute.CHECK_MENU.name
        ){
            val viewModel: RegisterMenuViewModel = it.sharedHiltViewModel(navController = navController)
            RegisterMenuCheckScreen(
                viewModel = viewModel,
                onBackPressed = { navController.navigateUp() }
            )
        }
    }
}