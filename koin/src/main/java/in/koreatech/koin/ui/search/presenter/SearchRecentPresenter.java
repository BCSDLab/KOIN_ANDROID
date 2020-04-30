package in.koreatech.koin.ui.search.presenter;

import java.util.ArrayList;
import java.util.List;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;

public class SearchRecentPresenter{
    private SearchRecentContract.View searchRecentView;
    private SearchArticleInteractor searchArticleInteractor;

    public SearchRecentPresenter(SearchRecentContract.View searchRecentView, SearchArticleInteractor searchArticleInteractor) {
        this.searchRecentView = searchRecentView;
        this.searchArticleInteractor = searchArticleInteractor;
        searchRecentView.setPresenter(this);
    }

    private final ApiCallback searchedRecentDataApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<String> recentSearch = (ArrayList<String>) object;
            searchRecentView.showSearchedArticle(recentSearch);
            searchRecentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            searchRecentView.hideLoading();
        }
    };

    public void getRecentData() {
        searchRecentView.showLoading();
        searchArticleInteractor.loadSavedSearchText(searchedRecentDataApiCallback);
    }
}
