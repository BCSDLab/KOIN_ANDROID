package `in`.koreatech.koin.feature.dining.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.dining.ui.diningdetail.DiningDetailScreen
import `in`.koreatech.koin.feature.dining.ui.diningnotice.DiningNoticeScreen

fun NavGraphBuilder.koinDiningGraph(
    navController: NavController
) {
    composable(
        route = "${DiningNavType.DiningDetail.route}?initDate={${INIT_DATE}}&initTabType={${INIT_TAB_TYPE}}",
        arguments = listOf(
            navArgument(INIT_DATE) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(INIT_TAB_TYPE) {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        val context = LocalContext.current
        val initialPage = it.arguments?.getInt(INIT_TAB_TYPE) ?: -1

        DiningDetailScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onTopbarActionClick = {
                navController.navigate(DiningNavType.DiningNotice.route)
            },
            initialPage = initialPage
        )
    }

    composable(
        route = DiningNavType.DiningNotice.route
    ) {
        val context = LocalContext.current

        DiningNoticeScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            }
        )
    }
}

const val INIT_DATE = "initDate"
const val INIT_TAB_TYPE = "initTabType"