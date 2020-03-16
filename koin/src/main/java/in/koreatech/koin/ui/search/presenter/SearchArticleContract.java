package in.koreatech.koin.ui.search.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.SearchedArticle;


public interface SearchArticleContract {
    interface View extends BaseView<SearchArticlePresenter> {
        void showLoading();

        void hideLoading();

        void showEmptyItem();

        void showSearchedArticle(SearchedArticle searchedArticle);

        void showRecentSearch();

        void showMessage(String message);
    }
}
