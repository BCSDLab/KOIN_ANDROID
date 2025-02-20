package `in`.koreatech.business.feature.event.writeevent.writeevent

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoViewModel
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.navigation.ADDEVENT
import `in`.koreatech.business.navigation.sharedHiltViewModel
import org.orbitmvi.orbit.compose.collectAsState


fun NavGraphBuilder.registerEventScreen(
    navController: NavHostController
) {
    navigation(
        route = "${ADDEVENT}/{storeId}",
        startDestination = "${RegisterEventRoute.REGISTER_EVENT.name}/{storeId}",
    ) {
        composable(
            route = "${RegisterEventRoute.REGISTER_EVENT.name}/{storeId}",
            arguments = listOf(
                navArgument("storeId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
        ) {
           // val storeId = it.arguments?.getInt("storeId") ?: -1
            val myStoreInfoViewModel: MyStoreDetailViewModel = it.sharedHiltViewModel(navController = navController)
            val myStoreInfoState = myStoreInfoViewModel.collectAsState().value

            WriteEventScreen(
                shopId = myStoreInfoState.storeId,
                onBackPressed = {
                    navController.navigateUp()
                },
                goToMyStoreScreen = {
                    navController.navigateUp()
                }
            )
        }

    }
}
