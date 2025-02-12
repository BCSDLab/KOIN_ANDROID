package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundReportRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundRequest
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArticleAuthApi {
    @GET("articles/keyword/me")
    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse

    @GET("articles/keyword/suggestions")
    suspend fun fetchKeywordSuggestions(): KeywordsResponse

    @POST("articles/keyword")
    suspend fun saveKeyword(@Body keywordRequest: ArticleKeywordRequest): ArticleKeywordWrapperResponse.ArticleKeywordResponse

    @DELETE("articles/keyword/{id}")
    suspend fun deleteKeyword(@Path("id") keywordId: Int): Response<Unit>

    @POST("articles/lost-item")
    suspend fun uploadArticleLostAndFound(
        @Body request: ArticleLostAndFoundRequest
    ): Response<ArticleLostAndFoundResponse>

    @DELETE("articles/lost-item/{id}")
    suspend fun deleteArticleLostAndFound(
        @Path("id") id: Int
    ): Response<Unit>

    @POST("articles/lost-item/{id}/reports")
    suspend fun reportLostAndFound(
        @Path("id") id: Int,
        @Body reportReasons: ArticleLostAndFoundReportRequest
    ): Response<Unit>
}