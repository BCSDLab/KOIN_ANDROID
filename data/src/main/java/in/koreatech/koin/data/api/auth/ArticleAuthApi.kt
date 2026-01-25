package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.article.ArticleKeywordRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundReportRequest
import `in`.koreatech.koin.data.request.article.ArticleLostAndFoundRequest
import `in`.koreatech.koin.data.request.article.ArticleModifyRequest
import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundPaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleAuthApi {
    @GET("articles/keyword/me")
    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse

    @GET("articles/keyword/suggestions")
    suspend fun fetchKeywordSuggestions(): KeywordsResponse

    @POST("articles/keyword")
    suspend fun saveKeyword(
        @Body keywordRequest: ArticleKeywordRequest
    ): ArticleKeywordWrapperResponse.ArticleKeywordResponse

    @DELETE("articles/keyword/{id}")
    suspend fun deleteKeyword(
        @Path("id") keywordId: Int
    ): Response<Unit>

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

    @POST("articles/lost-item/{id}/found")
    suspend fun updateItemFound(
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("articles/lost-item/{id}")
    suspend fun modifyArticleLostAndFound(
        @Path("id") id: Int,
        @Body request: ArticleModifyRequest
    ): Response<Unit>

    /**
     * 분실물 게시글 목록과 페이지 정보를 가져옴 (v2)
     * @param type 분실물 타입 (LOST: 분실물, FOUND: 습득물)
     * @param page 페이지 번호
     * @param limit 페이지 당 게시글 수
     * @param category 카테고리 (ALL, CARD, ID, WALLET, ELECTRONICS, ETC)
     * @param foundStatus 물품 상태 (ALL, FOUND, NOT_FOUND)
     * @param sort 정렬 순서 (LATEST, OLDEST)
     * @param author 작성자 필터 (ALL, MY)
     * @param title 게시글 제목 검색
     */
    @GET("articles/lost-item/v2")
    suspend fun fetchArticleLostAndFoundPaginationV2(
        @Query("type") type: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: List<String>,
        @Query("foundStatus") foundStatus: String?,
        @Query("sort") sort: String?,
        @Query("author") author: String?,
        @Query("title") title: String?
    ): ArticleLostAndFoundPaginationResponse
}
