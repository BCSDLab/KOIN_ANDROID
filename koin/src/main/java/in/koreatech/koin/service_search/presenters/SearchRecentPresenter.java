package in.koreatech.koin.service_search.presenters;

import android.content.Context;

import java.util.ArrayList;

import in.koreatech.koin.service_search.contracts.SearchRecentContract;
import in.koreatech.koin.service_search.ui.RecentSearchSharedPreference;

public class SearchRecentPresenter implements SearchRecentContract.Presenter {
    private SearchRecentContract.View searchRecentView;

    public SearchRecentPresenter(SearchRecentContract.View searchRecentView, Context context) {
        this.searchRecentView = searchRecentView;
        RecentSearchSharedPreference.getInstance().init(context);
        searchRecentView.setPresenter(this);
    }


    @Override
    public void getRecentData() {
        searchRecentView.showLoading();
        ArrayList<String> recentSearchs = RecentSearchSharedPreference.getInstance().getRecentSearch();
        searchRecentView.showSearchedArticle(recentSearchs);
        searchRecentView.hideLoading();
    }
}
