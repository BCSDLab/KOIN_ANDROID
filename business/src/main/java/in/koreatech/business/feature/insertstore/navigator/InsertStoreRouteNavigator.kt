package `in`.koreatech.business.feature.insertstore.navigator

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreen
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreen
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.business.feature.insertstore.selectcategory.SelectCategoryScreen
import `in`.koreatech.business.feature.insertstore.startinsetstore.StartInsertScreen
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo

@Composable
fun InsertStoreNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = InsertStoreRoute.START.name,
        modifier = modifier
    ) {

        composable(
            route = InsertStoreRoute.START.name,
        ) {
            StartInsertScreen(
                goToSelectCategoryScreen = {
                    navController.navigate(InsertStoreRoute.SELECT_CATEGORY.name)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = InsertStoreRoute.SELECT_CATEGORY.name,
        ) {
           SelectCategoryScreen(
               navigateToInsertBasicInfoScreen = {
                   navigateToMainInfo(navController, it)
               },
               onBackPressed = {
                   navController.navigateUp()
               }
           )
        }

        composable(
            route = "${InsertStoreRoute.BASIC_INFO.name}/{categoryId}",
            arguments = listOf(
                navArgument("categoryId"){
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ){
            InsertBasicInfoScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                navigateToInsertDetailInfoScreen = {
                    navigateToDetailInfo(navController, it)
                }
            )
        }

        composable(
            route = InsertStoreRoute.DETAIL_INFO.name
        ){
            InsertDetailInfoScreen(
                onBackPress = {
                    navController.navigateUp()
                }
            )
        }
    }
}

private fun navigateToMainInfo(
    navController: NavController,
    categoryId: Int
) {
    navController.navigate("${InsertStoreRoute.BASIC_INFO}/${categoryId}")
}

private fun navigateToDetailInfo(
    navController: NavController,
    storeBasicInfo: InsertBasicInfoScreenState
) {
    val bundle = Bundle()
    bundle.putParcelable("storeBasicInfo", storeBasicInfo)
    navController.navigate(InsertStoreRoute.DETAIL_INFO.name, args = bundle)
}


fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route = route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}