package in.koreatech.koin.service_search.contracts;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.SearchedArticle;


public interface SearchArticleContract {
    interface View extends BaseView<SearchArticleContract.Presenter> {
        void showLoading();

        void hideLoading();

        void showEmptyItem();

        void showSearchedArticle(SearchedArticle searchedArticle);

        void showRecentSearch();

        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void getArticleSearched(String searchText, int page);

        void saveText(String text);
    }
}
