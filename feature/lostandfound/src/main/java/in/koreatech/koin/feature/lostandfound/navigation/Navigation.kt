package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.lostandfound.DEEP_LINK_LOST_AND_FOUND_BASE
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.modify.LostAndFoundModify
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticle

fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController,
    onBackPressed: () -> Unit
) {
    composable<LostAndFoundNavType.LostAndFoundListRoute> { backStackEntry ->
        val refreshFlow = backStackEntry.savedStateHandle.getStateFlow(REFRESH_LIST, false).collectAsStateWithLifecycle()
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundList(
            doRefresh = refreshFlow.value,
            onTopbarBackClick = onBackPressed,
            navigateArticleDetail = { articleId ->
                navController.navigate(LostAndFoundNavType.LostAndFoundDetailRoute(articleId))
            },
            navigateToLogin = {
                navigator.navigateToSignIn(
                    context = context,
                    redirectUrl = DEEP_LINK_LOST_AND_FOUND_BASE
                ).apply {
                    context.startActivity(this)
                }
            },
            navigateToWrite = { typeName ->
                navController.navigate(LostAndFoundNavType.LostAndFoundWriteRoute(typeName))
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundDetailRoute> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundDetail(
            refreshLostAndFoundList = {
                navController.getBackStackEntry(LostAndFoundNavType.LostAndFoundListRoute)
                    ?.savedStateHandle
                    ?.set(REFRESH_LIST, true)
            },
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
            navigateToLogin = { articleId ->
                navigator.navigateToSignIn(
                    context = context,
                    redirectUrl = "$DEEP_LINK_LOST_AND_FOUND_BASE?id=$articleId"
                ).apply {
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
            onTopbarBackClick = onBackPressed,
            onSuccess = {
                navController.getBackStackEntry(LostAndFoundNavType.LostAndFoundListRoute)
                    ?.savedStateHandle
                    ?.set(REFRESH_LIST, true)
                navController.navigate(LostAndFoundNavType.LostAndFoundListRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundWriteRoute> {
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

    composable<LostAndFoundNavType.LostAndFoundModifyRoute> {
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
