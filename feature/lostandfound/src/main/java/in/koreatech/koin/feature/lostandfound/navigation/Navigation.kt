package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.modify.LostAndFoundModify
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticle

fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController,
    onBackPressed: () -> Unit
) {
    composable<LostAndFoundNavType.LostAndFoundListRoute> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundList(
            onTopbarBackClick = onBackPressed,
            navigateArticleDetail = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundDetailRoute(articleId))
            },
            navigateToLogin = {
                navigator.navigateToSignIn(context = context).apply { // TODO Add redirect url
                    context.startActivity(this)
                }
            },
            navigateToWrite = { typeName ->
                navController.navigate(LostAndFoundNavType.LostAndFoundWriteRoute(typeName))
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundDetailRoute> { backStackEntry ->
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundDetail(
            navigateToArticleList = {
                navController.navigate(LostAndFoundNavType.LostAndFoundListRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            },
            onTopbarBackClick = onBackPressed,
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
            },
            navigateToModify = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundModifyRoute(articleId))
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

    composable<LostAndFoundNavType.LostAndFoundWriteRoute> { backStackEntry ->
        LostAndFoundWriteArticle(
            onBackClick = onBackPressed,
            onComplete = {
                navController.navigate(LostAndFoundNavType.LostAndFoundListRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundModifyRoute> { backStackEntry ->
        LostAndFoundModify(
            onBackClick = onBackPressed,
            onComplete = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundListRoute) {
                    popUpTo<LostAndFoundNavType.LostAndFoundDetailRoute> {
                        inclusive = true
                    }
                }
                navController.navigate(LostAndFoundNavType.LostAndFoundDetailRoute(articleId))
            }
        )
    }
}
