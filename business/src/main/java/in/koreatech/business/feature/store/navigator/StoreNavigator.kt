package `in`.koreatech.business.feature.store.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoScreen
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoViewModel
import `in`.koreatech.business.feature.store.modifyinfo.ModifyOperatingTimeScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel


@Composable
fun ModifyInfoNavigator(
    modifier: Modifier,
) {
    val navController = rememberNavController()
    val modifyInfoViewModel = hiltViewModel<ModifyInfoViewModel>()
    val myStoreInfoViewModel = hiltViewModel<MyStoreDetailViewModel>()
    NavHost(
        navController = navController,
        startDestination = StoreRoute.MY_STORE.name,
        modifier = modifier
    ) {

        composable(
            route = StoreRoute.MY_STORE.name,
        ) {
            MyStoreDetailScreen(
                modifier = Modifier.fillMaxSize(),
                navigateToModifyScreen = { navController.navigate(StoreRoute.MODIFY_INFO.name) },
                viewModel = myStoreInfoViewModel,
                modifyInfoViewModel = modifyInfoViewModel
            )
        }

        composable(
            route = StoreRoute.MODIFY_INFO.name,
        ) {
            ModifyInfoScreen(
                viewModel = modifyInfoViewModel,
                onSettingOperatingClicked = { navController.navigate(StoreRoute.SETTING_OPERATING_TIME.name) },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = StoreRoute.SETTING_OPERATING_TIME.name,
        ) {
            ModifyOperatingTimeScreen(
                viewModel = modifyInfoViewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}