package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
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
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundDetail(
            navigateToArticleList = {
                navController.navigate(LostAndFoundNavType.LostAndFoundListRoute)
            },
            onTopbarBackClick = {
                navController.navigateUp()
            },
            navigateToChatRoom = { articleId ->

                val intent = navigator.navigateToChatRoom(context)
                intent.putExtra(CHAT_ARTICLE_ID, articleId)
                context.startActivity(intent)
            },
            navigateToRecentArticle = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundDetailRoute(articleId))
            },
            navigateToReport = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundReportRoute(articleId))
            },
            navigateToLogin = {
                navigator.navigateToSignIn(context = context).apply { // TODO Add redirect url
                    context.startActivity(this)
                }
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
