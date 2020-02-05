package in.koreatech.koin.service_search.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.SearchedArticle;


public interface SearchRecentContract {
    interface View extends BaseView<SearchRecentContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showSearchedArticle(ArrayList<String> recentSearchData);

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void getRecentData();
    }
}
