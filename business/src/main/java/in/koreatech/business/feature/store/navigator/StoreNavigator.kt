package `in`.koreatech.business.feature.store.navigator

import android.os.Bundle
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoScreen
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoViewModel
import `in`.koreatech.business.feature.store.modifyinfo.ModifyOperatingTimeScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailScreen
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.navigation.MYSTORESCREEN
import `in`.koreatech.business.navigation.REGISTERSTORESCREEN
import `in`.koreatech.business.navigation.navigate
import `in`.koreatech.business.navigation.sharedHiltViewModel
import `in`.koreatech.business.navigation.toNavigateModifyMenuScreen
import `in`.koreatech.business.navigation.toNavigateRegisterMenuScreen

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
                navigateToModifyScreen = {storeId ->
                    navController.navigate(StoreRoute.MODIFY_INFO.name)
                                         },
                viewModel = myStoreInfoViewModel,
                modifyInfoViewModel = modifyInfoViewModel,
                navigateToRegisterStoreScreen = {
                    navController.navigate(REGISTERSTORESCREEN)
                },
                navigateToRegisterMenuScreen = {  storeId ->
                    navController.toNavigateRegisterMenuScreen(storeId)
                },
                navigateToModifyMenuScreen = {menuId ->
                    navController.toNavigateModifyMenuScreen(menuId)

                }
            )
        }

        composable(
            route = StoreRoute.MODIFY_INFO.name
        ) {
            val modifyInfoViewModel: ModifyInfoViewModel = it.sharedHiltViewModel(navController = navController)
            val myStoreInfoViewModel: MyStoreDetailViewModel = it.sharedHiltViewModel(navController = navController)

            ModifyInfoScreen(
                viewModel = modifyInfoViewModel,
                storeInfoViewModel= myStoreInfoViewModel,
                onSettingOperatingClicked = { navController.navigate(StoreRoute.SETTING_OPERATING_TIME.name) },
                onBackClicked = { navController.popBackStack() },
                onModifyButtonCLicked = {
                    navController.navigate(StoreRoute.MY_STORE.name){
                        popUpTo(StoreRoute.MODIFY_INFO.name){
                            inclusive = true
                        }
                    }
                }
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

fun NavController.toNaviGateModifyStoreScreen(storeId: Int){
    val bundle = Bundle().apply {
        putInt("storeId", storeId)
    }
    navigate(StoreRoute.MODIFY_INFO.name, bundle)
}
fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodes = graph.findNode(MYSTORESCREEN) as? NavGraph

    val nodeId = nodes?.findNode(route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}