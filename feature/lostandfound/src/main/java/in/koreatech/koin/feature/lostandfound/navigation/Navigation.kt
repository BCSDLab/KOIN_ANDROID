package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport


fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController,
    navigator: Navigator
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
        val context = LocalContext.current
        LostAndFoundDetail(
            navigateToArticleList = {
                navController.navigate(LostAndFoundNavType.LostAndFoundList.route)
            },
            onTopbarBackClick = {
                navController.navigateUp()
            },
            navigateToChatRoom = { articleId ->
                val intent = navigator.navigateToChatRoom(context)
                intent.putExtra(ARTICLE_ID, articleId)
                context.startActivity(intent)
            },
            navigateToRecentArticle = { articleId ->
                navController.navigate("${LostAndFoundNavType.LostAndFoundDetail.route}/$articleId")
            },
            navigateToReport = { articleId ->
                navController.navigate("${LostAndFoundNavType.LostAndFoundReport.route}/$articleId")
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

const val ARTICLE_ID = "article_id"
