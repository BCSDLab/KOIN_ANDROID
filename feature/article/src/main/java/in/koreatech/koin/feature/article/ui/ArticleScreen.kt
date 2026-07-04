package `in`.koreatech.koin.feature.article.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.article.ui.article.detail.ArticleDetailScreen
import `in`.koreatech.koin.feature.article.ui.keyword.ArticleKeywordScreen
import `in`.koreatech.koin.feature.article.ui.list.ArticleListScreen
import `in`.koreatech.koin.feature.article.ui.search.ArticleSearchScreen

@Composable
fun ArticleScreen(onOldVersionClick: () -> Unit = {}) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ArticleRoute.LIST
    ) {
        composable(ArticleRoute.LIST) {
            ArticleListScreen(
                onArticleClick = { articleId, boardId ->
                    navController.navigate("${ArticleRoute.DETAIL_BASE}/$articleId/$boardId")
                },
                onSearchClick = { navController.navigate(ArticleRoute.SEARCH) },
                onKeywordManageClick = { navController.navigate(ArticleRoute.KEYWORD) },
                onOldVersionClick = onOldVersionClick
            )
        }
        composable(
            route = ArticleRoute.DETAIL,
            arguments = listOf(
                navArgument(ArticleRoute.ARG_ARTICLE_ID) { type = NavType.IntType },
                navArgument(ArticleRoute.ARG_BOARD_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt(ArticleRoute.ARG_ARTICLE_ID)
                ?: return@composable
            val boardId = backStackEntry.arguments?.getInt(ArticleRoute.ARG_BOARD_ID)
                ?: return@composable
            ArticleDetailScreen(
                articleId = articleId,
                boardId = boardId,
                onNavigateBack = { navController.popBackStack() },
                onListClick = { navController.popBackStack(ArticleRoute.LIST, false) },
                onHotArticleClick = { hotId, hotBoardId ->
                    navController.navigate("${ArticleRoute.DETAIL_BASE}/$hotId/$hotBoardId")
                }
            )
        }
        composable(ArticleRoute.SEARCH) {
            ArticleSearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onArticleClick = { articleId, boardId ->
                    navController.navigate("${ArticleRoute.DETAIL_BASE}/$articleId/$boardId")
                }
            )
        }
        composable(ArticleRoute.KEYWORD) {
            ArticleKeywordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

internal object ArticleRoute {
    const val LIST = "article_list"
    const val DETAIL_BASE = "article_detail"
    const val DETAIL = "article_detail/{articleId}/{boardId}"
    const val SEARCH = "article_search"
    const val KEYWORD = "article_keyword"
    const val ARG_ARTICLE_ID = "articleId"
    const val ARG_BOARD_ID = "boardId"
}
