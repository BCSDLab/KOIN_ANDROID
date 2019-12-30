package in.koreatech.koin.service_search.presenters;

import android.icu.text.SearchIterator;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.SearchedArticle;
import in.koreatech.koin.core.networks.interactors.SearchArticleInteractor;
import in.koreatech.koin.service_search.contracts.SearchResultContract;

public class SearchResultPresenter implements SearchResultContract.Presenter {

    private SearchResultContract.View searchResultView;
    private SearchArticleInteractor searchArticleInteractor;

    public SearchResultPresenter(SearchResultContract.View searchResultView, SearchArticleInteractor searchArticleInteractor) {
        this.searchResultView = searchResultView;
        this.searchArticleInteractor = searchArticleInteractor;
        searchResultView.setPresenter(this);
    }

    private final ApiCallback searchArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            SearchedArticle searchedArticle = (SearchedArticle) object;
            if(!searchedArticle.getSearchedArticles().isEmpty()) {
                searchResultView.showSearchedArticle(searchedArticle);

                searchResultView.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            searchResultView.showMessage("검색결과를 가져올 수 없습니다.");
            searchResultView.hideLoading();
        }
    };

    @Override
    public void getArticleSearched(String searchText, int page) {
        searchResultView.showLoading();
        searchArticleInteractor.readSearchArticleList(page, 1, searchText.trim(), searchArticleApiCallback);
    }
}
