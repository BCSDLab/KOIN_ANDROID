package in.koreatech.koin.ui.search.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.SearchedArticle;


public interface SearchResultContract {
    interface View extends BaseView<SearchResultPresenter> {
        void showLoading();

        void hideLoading();

        void showSearchedArticle(SearchedArticle searchedArticle);

        void showMessage(String message);

        void showMessage(@StringRes int  message);
    }
}
