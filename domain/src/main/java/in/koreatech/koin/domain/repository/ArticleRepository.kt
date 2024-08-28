package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): Flow<ArticlePagination>
    fun fetchArticle(articleId: Int, boardId: Int): Flow<Article>
    fun fetchPreviousArticle(articleId: Int, boardId: Int): Flow<Article>
    fun fetchNextArticle(articleId: Int, boardId: Int): Flow<Article>
    fun fetchHotArticleHeaders(): Flow<List<ArticleHeader>>
    fun fetchMyKeyword(): Flow<List<String>>
    fun fetchKeywordSuggestions(): Flow<List<String>>
    fun saveKeyword(keyword: String): Flow<Unit>
    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePagination
    suspend fun fetchHotKeywords(count: Int): List<String>
}