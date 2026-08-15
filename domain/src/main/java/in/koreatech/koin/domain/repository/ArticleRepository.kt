package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundStats
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.domain.model.article.ArticlePagination
import `in`.koreatech.koin.domain.model.article.KeywordType
import `in`.koreatech.koin.domain.model.article.LostAndFoundFilterParams
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun fetchArticlePagination(
        boardId: Int,
        page: Int,
        limit: Int
    ): Flow<ArticlePagination>

    fun fetchArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article>

    fun fetchArticleV2(
        articleId: Int,
        boardId: Int
    ): Flow<Article>

    fun fetchPreviousArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article>

    fun fetchNextArticle(
        articleId: Int,
        boardId: Int
    ): Flow<Article>

    fun fetchHotArticleHeaders(): Flow<List<ArticleHeader>>

    fun fetchMyKeyword(): Flow<List<String>>

    fun fetchMyLostItemKeyword(): Flow<List<String>>

    fun fetchKeywordSuggestions(type: KeywordType): Flow<List<String>>

    fun saveKeyword(type: KeywordType, keyword: String): Flow<Unit>

    fun deleteKeyword(type: KeywordType, keyword: String): Flow<Unit>

    fun fetchKeywordNotiIndex(): Flow<Int>

    fun saveKeywordNotiIndex(): Flow<Unit>

    fun fetchSearchedArticles(
        query: String,
        boardId: Int?,
        page: Int,
        limit: Int
    ): Flow<ArticlePagination>

    fun fetchMostSearchedKeywords(count: Int): Flow<List<String>>

    fun fetchSearchHistory(): Flow<List<String>>

    fun saveSearchHistory(query: String): Flow<Unit>

    fun deleteSearchHistory(query: String): Flow<Unit>

    fun clearSearchHistory(): Flow<Unit>

    fun fetchArticleLostAndFoundPaginationV2(
        filterParams: LostAndFoundFilterParams
    ): Flow<ArticleLostAndFoundPagination>

    fun fetchSearchedLostAndFoundArticles(
        query: String,
        page: Int,
        limit: Int
    ): Flow<ArticleLostAndFoundPagination>

    fun fetchArticleLostAndFoundV2(articleId: Int): Flow<ArticleLostAndFound>

    suspend fun uploadArticleLostAndFound(articleLostAndFoundList: List<ArticleLostAndFoundUpload>): Result<ArticleLostAndFound>

    suspend fun deleteArticleLostAndFound(articleId: Int): Result<Unit>

    suspend fun reportLostAndFoundArticle(
        articleId: Int,
        articleLostAndFoundList: List<ArticleLostAndFoundReportItem>
    ): Result<Unit>

    suspend fun fetchArticleLostAndFoundStats(): Result<ArticleLostAndFoundStats>

    suspend fun updateItemFound(articleId: Int): Result<Unit>

    suspend fun modifyArticleLostAndFound(
        articleId: Int,
        category: String,
        foundPlace: String,
        foundDate: String,
        content: String?,
        newImage: List<String>?,
        deleteImageIds: List<Int>?
    ): Result<Unit>
}
