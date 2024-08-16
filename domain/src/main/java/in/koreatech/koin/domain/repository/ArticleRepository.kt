package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.html.ArticleContent

interface ArticleRepository {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePagination
    suspend fun fetchArticle(articleId: Int): ArticleContent
    suspend fun fetchHotArticles(): List<ArticleHeader>
    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePagination
    suspend fun fetchHotKeywords(count: Int): List<String>
}