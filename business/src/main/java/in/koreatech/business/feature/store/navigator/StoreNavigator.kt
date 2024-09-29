package `in`.koreatech.business.feature.store.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoScreen
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoViewModel
import `in`.koreatech.business.feature.store.modifyinfo.ModifyOperatingTimeScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.navigation.MYSTORESCREEN
import `in`.koreatech.business.navigation.sharedHiltViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.myStoreScreen(
    navController: NavHostController
){
    navigation(
        route = MYSTORESCREEN,
        startDestination = StoreRoute.MY_STORE.name
    ){
        composable(
            route = StoreRoute.MY_STORE.name,
        ) {
            val myStoreInfoViewModel: MyStoreDetailViewModel = it.sharedHiltViewModel(navController = navController)
            val modifyInfoViewModel: ModifyInfoViewModel = it.sharedHiltViewModel(navController = navController)
            MyStoreDetailScreen(
                modifier = Modifier.fillMaxSize(),
                navigateToModifyScreen = { navController.navigate(StoreRoute.MODIFY_INFO.name) },
                viewModel = myStoreInfoViewModel,
                modifyInfoViewModel = modifyInfoViewModel
            )
        }

        composable(
            route = StoreRoute.MODIFY_INFO.name
        ) {
            val modifyInfoViewModel: ModifyInfoViewModel = it.sharedHiltViewModel(navController = navController)

            ModifyInfoScreen(
                viewModel = modifyInfoViewModel,
                onSettingOperatingClicked = { navController.navigate(StoreRoute.SETTING_OPERATING_TIME.name) },
                onBackClicked = { navController.popBackStack() }
            )
        }

        composable(
            route = StoreRoute.SETTING_OPERATING_TIME.name
        ) {
            val modifyInfoViewModel: ModifyInfoViewModel = it.sharedHiltViewModel(navController = navController)

            ModifyOperatingTimeScreen(
                viewModel = modifyInfoViewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}