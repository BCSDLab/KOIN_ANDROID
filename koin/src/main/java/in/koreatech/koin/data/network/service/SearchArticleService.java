package in.koreatech.koin.data.network.service;

import in.koreatech.koin.data.network.entity.SearchedArticle;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static in.koreatech.koin.constant.URLConstant.SEARCH.ARTICLESEARCH;

public interface SearchArticleService {
    /**
     * @param page       페이지 숫자
     * @param searchType 검색 타입으로 0 - 제목, 1 - 제목 + 내용, 2 - 작성자
     * @param query      검색한 단어
     * @return
     */
    @GET(ARTICLESEARCH)
    Observable<SearchedArticle> getSearchedArticles(@Query("page") int page, @Query("searchType") int searchType, @Query("query") String query);

}
