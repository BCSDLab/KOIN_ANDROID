package in.koreatech.koin.service_search.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.SearchedArticle;


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
