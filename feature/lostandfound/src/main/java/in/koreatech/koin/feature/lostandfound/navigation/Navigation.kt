package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport


fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController
) {
    composable(
        route = LostAndFoundNavType.LostAndFoundList.route,
    ) {
        LostAndFoundList()
    }

    composable(
        route = "${LostAndFoundNavType.LostAndFoundDetail.route}/{$ARTICLE_ID}",
        arguments = listOf(
            navArgument(ARTICLE_ID) { type = NavType.IntType }
        )
    ) {
        LostAndFoundDetail(
            onTopbarBackClick = {
                navController.navigateUp()
            }
        )
    }

    composable(
        route = "${LostAndFoundNavType.LostAndFoundReport.route}/{$ARTICLE_ID}",
        arguments = listOf(
            navArgument(ARTICLE_ID) { type = NavType.IntType }
        )
    ) {
        val articleId = it.arguments?.getInt(ARTICLE_ID) ?: -1
        LostAndFoundReport(
            articleId = articleId,
            onSuccess = { navController.navigateUp() }
        )
    }

    composable(
        route = LostAndFoundNavType.LostAndFoundWrite.route,
    ) {

    }

}

const val ARTICLE_ID = "articleId"
