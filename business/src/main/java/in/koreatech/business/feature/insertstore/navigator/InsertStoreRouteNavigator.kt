package `in`.koreatech.business.feature.insertstore.navigator

import android.os.Build
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.insertstore.finalcheckstore.FinalCheckStoreScreen
import `in`.koreatech.business.feature.insertstore.finalcheckstore.FinalCheckStoreScreenImpl
import `in`.koreatech.business.feature.insertstore.finishregisterstore.FinishRegisterScreen
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreen
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreenState
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreenViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeSettingScreen
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreen
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.business.feature.insertstore.selectcategory.SelectCategoryScreen
import `in`.koreatech.business.feature.insertstore.startinsetstore.StartInsertScreen
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InsertStoreNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    detailInfoScreenViewModel: InsertDetailInfoScreenViewModel = hiltViewModel()
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
            val bundle = it.arguments

            val basicInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable("storeBasicInfo", InsertBasicInfoScreenState::class.java)
            } else {
                bundle?.getParcelable("storeBasicInfo") as? InsertBasicInfoScreenState
            }

            detailInfoScreenViewModel.getStoreBasicInfo(basicInfo)

            InsertDetailInfoScreen(
                reviseButtonClicked = {
                    navController.navigate(InsertStoreRoute.OPERATING_TIME.name)
                },
                onBackPressed = {
                    navController.navigateUp()
                },

                viewModel = detailInfoScreenViewModel,

                navigateToCheckScreen = { storeInfo ->
                    navigateToCheckScreen(navController, storeInfo)
                }
            )
        }

        composable(
            route = InsertStoreRoute.OPERATING_TIME.name

        ){
            OperatingTimeSettingScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                viewModel = detailInfoScreenViewModel
            )
        }

        composable(
            route = InsertStoreRoute.CHECK_SCREEN.name
        ){
            FinalCheckStoreScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                navigateToFinishScreen = {
                    navController.navigate(InsertStoreRoute.FINISH_SCREEN.name)
                }
            )
        }

        composable(
            route = InsertStoreRoute.FINISH_SCREEN.name,
        ) {
            FinishRegisterScreen(
                goToMainScreen = {

                },
                onBackPressed = {
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

private fun navigateToCheckScreen(
    navController: NavController,
    storeInfo: InsertDetailInfoScreenState
) {
    val bundle = Bundle()
    bundle.putParcelable("storeInfo", storeInfo)
    navController.navigate(InsertStoreRoute.CHECK_SCREEN.name, args = bundle)
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