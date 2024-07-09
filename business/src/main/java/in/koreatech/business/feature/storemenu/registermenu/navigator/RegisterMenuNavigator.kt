package `in`.koreatech.business.feature.storemenu.registermenu.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.storemenu.registermenu.registermenu.RegisterMenuScreen
import `in`.koreatech.business.feature.storemenu.registermenu.registermenu.RegisterMenuViewModel

@Composable
fun RegisterMenuNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    registerMenuViewModel: RegisterMenuViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = RegisterMenuRoute.INSERT_MENU.name,
        modifier = modifier
    ) {

        composable(
            route = RegisterMenuRoute.INSERT_MENU.name,
        ) {
            RegisterMenuScreen(
                viewModel = registerMenuViewModel,
                onBackPressed = {}
            )
        }
    }
}