package `in`.koreatech.koin.feature.article.ui.article.state

import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState
import `in`.koreatech.koin.feature.article.model.toArticleHeaderState

data class ArticlePaginationState(
    val articles: List<ArticleHeaderState>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)

fun ArticlePagination.toArticlePaginationState() = ArticlePaginationState(
    articles = articleHeaders.map { it.toArticleHeaderState() },
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)
