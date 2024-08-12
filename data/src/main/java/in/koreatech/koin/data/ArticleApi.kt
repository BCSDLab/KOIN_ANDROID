package `in`.koreatech.koin.data

import `in`.koreatech.koin.data.response.article.ArticlePaginationResponse
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.data.response.article.KeywordsResponse
import retrofit2.http.GET

interface ArticleApi {

    /**
     * 게시글 목록과 페이지 정보를 가져옴
     * @param boardId 게시판 아이디
     * @param page 페이지 번호
     * @param limit 페이지 당 게시글 수
     */
    @GET("articles")
    suspend fun fetchArticlePagination(boardId: Int, page: Int, limit: Int): ArticlePaginationResponse

    @GET("articles/{articleId}")
    suspend fun fetchArticle(articleId: Int): ArticleResponse

    @GET("articles/hot")
    suspend fun fetchHotArticles(): List<ArticleResponse>

    /**
     * 검색된 게시글 목록과 페이지 정보를 가져옴
     * @param query 검색어
     */
    @GET("articles/search")
    suspend fun fetchSearchedArticles(query: String, page: Int, limit: Int): ArticlePaginationResponse

    /**
     * 많이 검색되는 키워드
     * @param count 키워드 수
     */
    @GET("articles/hot/keyword")
    suspend fun fetchHotKeywords(count: Int): KeywordsResponse
}