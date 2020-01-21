package in.koreatech.koin.ui.search.presenter;

import java.util.ArrayList;

import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;

public class SearchRecentPresenter{
    private SearchRecentContract.View searchRecentView;

    public SearchRecentPresenter(SearchRecentContract.View searchRecentView) {
        this.searchRecentView = searchRecentView;
        searchRecentView.setPresenter(this);
    }

    public void getRecentData() {
        searchRecentView.showLoading();
        ArrayList<String> recentSearchs = RecentSearchSharedPreference.getInstance().getRecentSearch();
        searchRecentView.showSearchedArticle(recentSearchs);
        searchRecentView.hideLoading();
    }
}
