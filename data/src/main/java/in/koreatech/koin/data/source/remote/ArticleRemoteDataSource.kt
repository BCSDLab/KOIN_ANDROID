package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ArticleApi
import `in`.koreatech.koin.data.api.auth.ArticleAuthApi
import `in`.koreatech.koin.data.mapper.toArticleLostAndFoundRequest
import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundReportRequest
import `in`.koreatech.koin.data.request.article.ArticleModifyRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundPaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import `in`.koreatech.koin.domain.model.article.KeywordType
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val articleApi: ArticleApi,
    private val articleAuthApi: ArticleAuthApi
) {
    suspend fun fetchArticlePagination(
        boardId: Int,
        page: Int,
        limit: Int
    ): ArticlePaginationResponse {
        return articleApi.fetchArticlePagination(boardId, page, limit)
    }

    suspend fun fetchArticle(
        articleId: Int,
        boardId: Int
    ): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchPreviousArticle(
        articleId: Int,
        boardId: Int
    ): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchNextArticle(
        articleId: Int,
        boardId: Int
    ): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchHotArticles(): List<ArticleResponse> {
        return articleApi.fetchHotArticles()
    }

    suspend fun fetchMyKeyword(type: KeywordType): ArticleKeywordWrapperResponse {
        return articleAuthApi.fetchMyKeyword(type)
    }

    suspend fun fetchKeywordSuggestions(type: KeywordType): KeywordsResponse {
        return articleAuthApi.fetchKeywordSuggestions(type)
    }

    suspend fun saveKeyword(type: KeywordType, keyword: String): ArticleKeywordWrapperResponse.ArticleKeywordResponse {
        return articleAuthApi.saveKeyword(type, ArticleKeywordRequest(keyword))
    }

    suspend fun deleteKeyword(id: Int) {
        articleAuthApi.deleteKeyword(id)
    }

    suspend fun fetchSearchedArticles(
        query: String,
        boardId: Int?,
        page: Int,
        limit: Int
    ): ArticlePaginationResponse {
        return articleApi.fetchSearchedArticles(query, boardId, page, limit)
    }

    suspend fun fetchMostSearchedKeywords(count: Int): KeywordsResponse {
        return articleApi.fetchMostSearchedKeywords(count)
    }

    suspend fun fetchArticleLostAndFoundPaginationV2(
        type: String?,
        page: Int,
        limit: Int,
        category: List<String>,
        foundStatus: String?,
        sort: String?,
        author: String?,
        title: String?
    ): ArticleLostAndFoundPaginationResponse {
        return articleAuthApi.fetchArticleLostAndFoundPaginationV2(
            type = type,
            page = page,
            limit = limit,
            category = category,
            foundStatus = foundStatus,
            sort = sort,
            author = author,
            title = title?.takeIf { it.isNotBlank() }
        )
    }

    suspend fun fetchSearchedLostAndFoundArticles(
        query: String,
        page: Int,
        limit: Int
    ): ArticleLostAndFoundPaginationResponse {
        return articleApi.fetchSearchedLostAndFoundArticles(query, page, limit)
    }

    suspend fun fetchArticleLostAndFoundV2(articleId: Int): ArticleLostAndFoundResponse {
        return articleAuthApi.fetchArticleLostAndFoundV2(articleId)
    }

    suspend fun uploadArticleLostAndFound(articleLostAndFound: List<ArticleLostAndFoundUpload>): Result<ArticleLostAndFoundResponse> {
        return try {
            val response = articleAuthApi.uploadArticleLostAndFound(articleLostAndFound.toArticleLostAndFoundRequest())
            Result.success(response.body()!!)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun deleteArticleLostAndFound(articleId: Int): Result<Unit> {
        return try {
            articleAuthApi.deleteArticleLostAndFound(articleId)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun reportLostAndFoundArticle(
        articleId: Int,
        reportReasons: ArticleLostAndFoundReportRequest
    ): Result<Unit> {
        return try {
            val response = articleAuthApi.reportLostAndFound(articleId, reportReasons)
            if (response.isSuccessful) {
                return Result.success(Unit)
            }
            return Result.failure(Exception(response.message()))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun fetchArticleLostAndFoundStats() = articleApi.fetchArticleLostAndFoundStats()

    suspend fun updateItemFound(
        articleId: Int
    ) = articleAuthApi.updateItemFound(articleId)

    suspend fun modifyArticleLostAndFound(
        articleId: Int,
        request: ArticleModifyRequest
    ) = articleAuthApi.modifyArticleLostAndFound(articleId, request)
}
