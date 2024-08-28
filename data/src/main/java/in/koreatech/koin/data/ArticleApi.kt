package `in`.koreatech.koin.data

import `in`.koreatech.koin.data.response.article.ArticleKeywordWrapperResponse
import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApi {

    /**
     * 게시글 목록과 페이지 정보를 가져옴
     * @param boardId 게시판 아이디
     * @param page 페이지 번호
     * @param limit 페이지 당 게시글 수
     */
    @GET("articles")
    suspend fun fetchArticlePagination(
        @Query("boardId") boardId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ArticlePaginationResponse

    /**
     * 단일 게시글 조회
     * @param id 게시글 아이디
     * @param boardId 게시판 아이디, 이전 및 다음 게시글을 판별하기 위함
     */
    @GET("articles/{id}")
    suspend fun fetchArticle(
        @Path("id") articleId: Int,
        @Query("boardId") boardId: Int
    ): ArticleResponse

    @GET("articles/hot")
    suspend fun fetchHotArticles(): List<ArticleResponse>

    @GET("articles/keyword/me")
    suspend fun fetchMyKeyword(): ArticleKeywordWrapperResponse

    /**
     * 검색된 게시글 목록과 페이지 정보를 가져옴
     * @param query 검색어
     * @param page 페이지 번호
     * @param limit 페이지 당 게시글 수
     */
    @GET("articles/search")
    suspend fun fetchSearchedArticles(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ArticlePaginationResponse

    /**
     * 많이 검색되는 키워드
     * @param count 키워드 수
     */
    @GET("articles/hot/keyword")
    suspend fun fetchHotKeywords(@Query("count") count: Int): KeywordsResponse
}