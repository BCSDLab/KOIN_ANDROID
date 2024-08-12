package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.ArticleApi
import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import javax.inject.Inject

class ArticleRemoteDataSource @Inject constructor(
    private val articleApi: ArticleApi
) {
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePaginationResponse {
        return articleApi.fetchArticlePagination(boardId, page, limit)
    }

    suspend fun fetchArticle(articleId: Int): ArticleResponse {
        return articleApi.fetchArticle(articleId)
    }

    suspend fun fetchHotArticles(): List<ArticleResponse> {
        return articleApi.fetchHotArticles()
    }

    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePaginationResponse {
        return articleApi.fetchSearchedArticles(query, page, limit)
    }

    suspend fun fetchHotKeywords(count: Int): KeywordsResponse {
        return articleApi.fetchHotKeywords(count)
    }
}