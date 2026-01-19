package `in`.koreatech.koin.feature.lostandfound.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticle

fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController
) {
    composable<LostAndFoundNavType.LostAndFoundListRoute> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundList(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
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
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
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

    composable<LostAndFoundNavType.LostAndFoundWriteRoute> { backStackEntry ->
        val context = LocalContext.current
        LostAndFoundWriteArticle(
            onBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
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
}
