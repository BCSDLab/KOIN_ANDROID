package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundReportRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ArticleAuthApi {
    @GET(URLConstant.ARTICLES.KEYWORD_ME)
    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse

    @GET(URLConstant.ARTICLES.SUGGESTIONS)
    suspend fun fetchKeywordSuggestions(): KeywordsResponse

    @POST(URLConstant.ARTICLES.KEYWORD)
    suspend fun saveKeyword(
        @Body keywordRequest: ArticleKeywordRequest
    ): ArticleKeywordWrapperResponse.ArticleKeywordResponse

    @DELETE(URLConstant.ARTICLES.KEYWORD_ID)
    suspend fun deleteKeyword(
        @Path("id") keywordId: Int
    ): Response<Unit>

    @POST(URLConstant.ARTICLES.LOSTANDFOUND)
    suspend fun uploadArticleLostAndFound(
        @Body request: ArticleLostAndFoundRequest
    ): Response<ArticleLostAndFoundResponse>

    @DELETE(URLConstant.ARTICLES.LOSTANDFOUND_ID)
    suspend fun deleteArticleLostAndFound(
        @Path("id") id: Int
    ): Response<Unit>

    @POST(URLConstant.ARTICLES.LOSTANDFOUND_REPORTS)
    suspend fun reportLostAndFound(
        @Path("id") id: Int,
        @Body reportReasons: ArticleLostAndFoundReportRequest
    ): Response<Unit>
}
