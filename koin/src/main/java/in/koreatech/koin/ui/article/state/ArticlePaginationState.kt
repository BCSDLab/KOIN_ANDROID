package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.ArticlePagination

data class ArticlePaginationState(
    val articles: List<ArticleState>,
    val totalCount: Int,
    val currentCount: Int,
    val totalPage: Int,
    val currentPage: Int
)

fun ArticlePagination.toArticlePaginationState() = ArticlePaginationState(
    articles = articles.map { it.toArticleState() },
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)
