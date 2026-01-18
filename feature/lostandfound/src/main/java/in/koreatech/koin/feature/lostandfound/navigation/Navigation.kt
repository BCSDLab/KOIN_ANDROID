package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport

fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController
) {
    composable<LostAndFoundNavType.LostAndFoundListRoute> {
        LostAndFoundList()
    }

    composable<LostAndFoundNavType.LostAndFoundDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<LostAndFoundNavType.LostAndFoundDetailRoute>()
        LostAndFoundDetail(
            onTopbarBackClick = {
                navController.navigateUp()
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundReportRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<LostAndFoundNavType.LostAndFoundDetailRoute>()
        LostAndFoundReport(
            articleId = route.articleId,
            onSuccess = { navController.navigateUp() }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundWriteRoute> {
    }
}

const val ARTICLE_ID = "articleId"
