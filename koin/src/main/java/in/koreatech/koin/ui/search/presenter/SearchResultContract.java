package in.koreatech.koin.ui.search.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.SearchedArticle;


public interface SearchResultContract {
    interface View extends BaseView<SearchResultContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showSearchedArticle(SearchedArticle searchedArticle);


        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void getArticleSearched(String searchText, int page);
    }
}
