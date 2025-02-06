package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
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
    fun deleteKeyword(keyword: String): Flow<Unit>
    fun fetchKeywordNotiIndex(): Flow<Int>
    fun saveKeywordNotiIndex(): Flow<Unit>
    fun fetchSearchedArticles(query: String, boardId: Int, page: Int, limit: Int): Flow<ArticlePagination>
    fun fetchMostSearchedKeywords(count: Int): Flow<List<String>>
    fun fetchSearchHistory(): Flow<List<String>>
    fun saveSearchHistory(query: String): Flow<Unit>
    fun deleteSearchHistory(query: String): Flow<Unit>
    fun clearSearchHistory(): Flow<Unit>
    fun fetchArticleLostAndFoundPagination(page: Int, limit: Int, type: String?): Flow<ArticleLostAndFoundPagination>
    fun fetchArticleLostAndFound(articleId: Int): Flow<ArticleLostAndFound>
    suspend fun uploadArticleLostAndFound(articleLostAndFoundList: List<ArticleLostAndFoundUpload>): Result<ArticleLostAndFound>
    suspend fun deleteArticleLostAndFound(articleId: Int): Result<Unit>
    suspend fun reportLostAndFoundArticle(articleId: Int, articleLostAndFoundList: List<ArticleLostAndFoundReportItem>): Result<Unit>
}