package `in`.koreatech.business.feature.store.modifyinfo.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoScreen
import `in`.koreatech.business.feature.store.modifyinfo.ModifyOperatingTimeScreen


@Composable
fun ModifyInfoNavigator(
    modifier: Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ModifyInfoRoute.MODIFY_INFO.name,
        modifier = modifier
    ) {

        composable(
            route = ModifyInfoRoute.MODIFY_INFO.name,
        ) {
            ModifyInfoScreen(
                onSettingOperatingClicked = { navController.navigate(ModifyInfoRoute.SETTING_OPERATING_TIME.name) }
            )
        }

        composable(
            route = ModifyInfoRoute.SETTING_OPERATING_TIME.name,
        ) {
            ModifyOperatingTimeScreen(
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}