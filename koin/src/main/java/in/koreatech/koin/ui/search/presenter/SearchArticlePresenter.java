package in.koreatech.koin.ui.search.presenter;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;
import in.koreatech.koin.util.FormValidatorUtil;

public class SearchArticlePresenter {
    private SearchArticleContract.View searchArticleView;
    private SearchArticleInteractor searchArticleInteractor;

    public SearchArticlePresenter(SearchArticleContract.View searchArticleView, SearchArticleInteractor searchArticleInteractor) {
        this.searchArticleView = searchArticleView;
        this.searchArticleInteractor = searchArticleInteractor;
        searchArticleView.setPresenter(this);
    }

    private final ApiCallback searchArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            SearchedArticle searchedArticle = (SearchedArticle) object;
            if (searchedArticle.getSearchedArticles().isEmpty()) {
                searchArticleView.showEmptyItem();
            } else
                searchArticleView.showSearchedArticle(searchedArticle);

            searchArticleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            searchArticleView.showEmptyItem();
            searchArticleView.hideLoading();
        }
    };

    private final ApiCallback saveTextApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
        }

        @Override
        public void onFailure(Throwable throwable) {
            searchArticleView.showMessage(R.string.search_save_recent_fail);
        }
    };

    public void getArticleSearched(String searchText, int page) {
        searchArticleView.showLoading();
        if (FormValidatorUtil.validateStringIsEmpty(searchText)) {
            searchArticleView.showRecentSearch();
            searchArticleView.hideLoading();
            return;
        }
        try {
            String urlEncodedText = URLEncoder.encode(searchText.trim(), "UTF-8");
            searchArticleInteractor.readSearchArticleList(page, 1, urlEncodedText, searchArticleApiCallback);
        } catch (UnsupportedEncodingException e) {
            searchArticleView.showMessage(R.string.search_fail);
        }
    }

    public void saveSearchedText(String text) {
        if (FormValidatorUtil.validateStringIsEmpty(text)) return;
        searchArticleInteractor.saveSearchText(text, saveTextApiCallback);
    }

}
