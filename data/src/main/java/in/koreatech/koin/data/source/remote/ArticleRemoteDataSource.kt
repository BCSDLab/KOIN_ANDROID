package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ArticleApi
import `in`.koreatech.koin.data.api.auth.ArticleAuthApi
import `in`.koreatech.koin.data.mapper.toArticleLostAndFoundRequest
import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundReportRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundPaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundUpload
import timber.log.Timber
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

    suspend fun fetchArticle(articleId: Int, boardId: Int): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchPreviousArticle(articleId: Int, boardId: Int): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchNextArticle(articleId: Int, boardId: Int): ArticleResponse {
        return articleApi.fetchArticle(articleId, boardId)
    }

    suspend fun fetchHotArticles(): List<ArticleResponse> {
        return articleApi.fetchHotArticles()
    }

    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse {
        return articleAuthApi.fetchMyKeyword()
    }

    suspend fun fetchKeywordSuggestions(): KeywordsResponse {
        return articleAuthApi.fetchKeywordSuggestions()
    }

    suspend fun saveKeyword(keyword: String): ArticleKeywordWrapperResponse.ArticleKeywordResponse {
        return articleAuthApi.saveKeyword(ArticleKeywordRequest(keyword))
    }

    suspend fun deleteKeyword(id: Int) {
        articleAuthApi.deleteKeyword(id)
    }

    suspend fun fetchSearchedArticles(
        query: String,
        boardId: Int,
        page: Int,
        limit: Int
    ): ArticlePaginationResponse {
        return articleApi.fetchSearchedArticles(query, boardId, page, limit)
    }

    suspend fun fetchMostSearchedKeywords(count: Int): KeywordsResponse {
        return articleApi.fetchMostSearchedKeywords(count)
    }

    suspend fun fetchArticleLostAndFoundPagination(
        page: Int,
        limit: Int,
        type: String?
    ): ArticleLostAndFoundPaginationResponse {
        return articleApi.fetchArticleLostAndFoundPagination(page, limit, type)
    }

    suspend fun fetchArticleLostAndFound(articleId: Int): ArticleLostAndFoundResponse {
        return articleApi.fetchArticleLostAndFound(articleId)
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

    suspend fun reportLostAndFoundArticle(articleId: Int, reportReasons: ArticleLostAndFoundReportRequest): Result<Unit> {
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
}