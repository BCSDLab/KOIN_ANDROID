package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.html.ArticleContent
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePagination
    suspend fun fetchArticle(articleId: Int): ArticleContent
    fun fetchHotArticleHeaders(): Flow<List<ArticleHeader>>
    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePagination
    suspend fun fetchHotKeywords(count: Int): List<String>
}