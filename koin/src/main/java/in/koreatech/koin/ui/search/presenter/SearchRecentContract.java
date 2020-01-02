package in.koreatech.koin.ui.search.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;


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
