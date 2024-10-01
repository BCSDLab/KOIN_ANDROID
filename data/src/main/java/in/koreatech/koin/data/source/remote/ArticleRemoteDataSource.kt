package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ArticleApi
import `in`.koreatech.koin.data.api.auth.ArticleAuthApi
import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.AttachmentResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val articleApi: ArticleApi,
    private val articleAuthApi: ArticleAuthApi
) {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePaginationResponse {
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

    suspend fun fetchSearchedArticles(query: String, boardId: Int, page: Int, limit: Int): ArticlePaginationResponse {
        return articleApi.fetchSearchedArticles(query, boardId, page, limit)
    }

    suspend fun fetchMostSearchedKeywords(count: Int): KeywordsResponse {
        return articleApi.fetchMostSearchedKeywords(count)
    }
}