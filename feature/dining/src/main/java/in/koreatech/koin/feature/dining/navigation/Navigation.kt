package `in`.koreatech.koin.feature.dining.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.dining.ui.diningdetail.DiningDetailScreen
import `in`.koreatech.koin.feature.dining.ui.diningnotice.DiningNoticeScreen

fun NavGraphBuilder.koinDiningGraph(
    navController: NavController
) {
    composable<DiningNavType.DiningDetail> { entry ->
        val args = entry.toRoute<DiningNavType.DiningDetail>()
        val context = LocalContext.current

        DiningDetailScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onTopbarActionClick = {
                navController.navigate(DiningNavType.DiningNotice)
            },
            initialPage = args.initTabType
        )
    }

    composable<DiningNavType.DiningNotice> {
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
