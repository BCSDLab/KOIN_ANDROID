package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundRequest
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundPaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleAuthApi {
    @GET("articles/keyword/me")
    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse

    @GET("articles/keyword/suggestions")
    suspend fun fetchKeywordSuggestions(): KeywordsResponse

    @POST("articles/keyword")
    suspend fun saveKeyword(@Body keywordRequest: ArticleKeywordRequest): ArticleKeywordWrapperResponse.ArticleKeywordResponse

    @DELETE("articles/keyword/{id}")
    suspend fun deleteKeyword(@Path("id") keywordId: Int): Response<Unit>

    /**
     * 분실물 게시글 목록과 페이지 정보를 가져옴
     * @param page 페이지 번호
     * @param limit 페이지 당 게시글 수
     */
    @GET("articles/lost-item")
    suspend fun fetchArticleLostAndFoundPagination(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String?
    ): ArticleLostAndFoundPaginationResponse

    @POST("articles/lost-item")
    suspend fun uploadArticleLostAndFound(
        @Body request: ArticleLostAndFoundRequest
    ): Response<ArticleLostAndFoundResponse>

    @DELETE("articles/lost-item/{id}")
    suspend fun deleteArticleLostAndFound(
        @Path("id") id: Int
    ): Response<Unit>
}