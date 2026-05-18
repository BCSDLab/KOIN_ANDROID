package `in`.koreatech.koin.feature.lostandfound.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.lostandfound.DEEP_LINK_LOST_AND_FOUND_BASE
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetail
import `in`.koreatech.koin.feature.lostandfound.ui.keyword.LostAndFoundKeyword
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundList
import `in`.koreatech.koin.feature.lostandfound.ui.list.LostAndFoundListViewModel
import `in`.koreatech.koin.feature.lostandfound.ui.modify.LostAndFoundModify
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticle

fun NavGraphBuilder.koinLostAndFoundGraph(
    navController: NavController,
    onBackPressed: () -> Unit
) {
    val cancelRefreshList = { cancelRefresh: Boolean ->
        navController.getBackStackEntry(LostAndFoundNavType.LostAndFoundListRoute)
            ?.savedStateHandle
            ?.let { handle ->
                if (handle.get<Boolean>(CANCEL_REFRESH_LIST) != false) {
                    handle[CANCEL_REFRESH_LIST] = cancelRefresh
                }
            }
    }

    val onBackPressedWithCancel = {
        cancelRefreshList(true)
        onBackPressed()
    }

    val navigateToList = { cancelRefresh: Boolean ->
        cancelRefreshList(cancelRefresh)
        navController.navigate(LostAndFoundNavType.LostAndFoundListRoute) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    composable<LostAndFoundNavType.LostAndFoundListRoute> { backStackEntry ->
        // hiltViewModel()을 한 번만 호출하여 LostAndFoundList에 명시적으로 전달
        val listViewModel: LostAndFoundListViewModel = hiltViewModel()
        var isCancelRefresh by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (backStackEntry.savedStateHandle.contains(CANCEL_REFRESH_LIST)) {
                isCancelRefresh = backStackEntry.savedStateHandle[CANCEL_REFRESH_LIST] ?: false
            }
            backStackEntry.savedStateHandle.remove<Boolean>(CANCEL_REFRESH_LIST)
        }
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundList(
            cancelRefresh = isCancelRefresh,
            viewModel = listViewModel,
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
            },
            navigateToKeywordSetting = {
                navController.navigate(LostAndFoundNavType.LostAndFoundKeywordRoute)
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundDetailRoute> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundDetail(
            navigateToArticleList = navigateToList,
            onTopbarBackClick = onBackPressedWithCancel,
            refreshList = { cancelRefreshList(false) },
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
        val route = backStackEntry.toRoute<LostAndFoundNavType.LostAndFoundReportRoute>()
        LostAndFoundReport(
            articleId = route.articleId,
            onTopbarBackClick = onBackPressedWithCancel,
            onSuccess = { navigateToList(false) }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundWriteRoute> {
        LostAndFoundWriteArticle(
            onBackClick = onBackPressedWithCancel,
            onComplete = { navigateToList(false) }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundModifyRoute> {
        LostAndFoundModify(
            onBackClick = onBackPressedWithCancel,
            onComplete = { articleId ->
                navigateToList(false)
                navController.navigate(LostAndFoundNavType.LostAndFoundDetailRoute(articleId))
            }
        )
    }

    composable<LostAndFoundNavType.LostAndFoundKeywordRoute> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        LostAndFoundKeyword(
            viewModel = hiltViewModel(),
            onBackClick = { if (!navController.popBackStack()) onBackPressed() },
            navigateToLogin = {
                navigator.navigateToSignIn(
                    context = context,
                    redirectUrl = DEEP_LINK_LOST_AND_FOUND_BASE
                ).apply {
                    context.startActivity(this)
                }
            }
        )
    }
}
