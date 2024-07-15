package `in`.koreatech.business.feature.store.modifyinfo.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoScreen
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoViewModel
import `in`.koreatech.business.feature.store.modifyinfo.ModifyOperatingTimeScreen


@Composable
fun ModifyInfoNavigator(
    modifier: Modifier,
) {
    val navController = rememberNavController()
    val modifyInfoViewModel = hiltViewModel<ModifyInfoViewModel>()
    NavHost(
        navController = navController,
        startDestination = ModifyInfoRoute.MODIFY_INFO.name,
        modifier = modifier
    ) {

        composable(
            route = ModifyInfoRoute.MODIFY_INFO.name,
        ) {
            ModifyInfoScreen(
                viewModel = modifyInfoViewModel,
                onSettingOperatingClicked = { navController.navigate(ModifyInfoRoute.SETTING_OPERATING_TIME.name) }
            )
        }

        composable(
            route = ModifyInfoRoute.SETTING_OPERATING_TIME.name,
        ) {
            ModifyOperatingTimeScreen(
                viewModel = modifyInfoViewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}