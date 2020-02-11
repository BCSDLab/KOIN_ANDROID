package in.koreatech.koin.ui.search.presenter;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;

public class SearchArticlePresenter {
    public static final int MAX_SAVE_SIZE = 20;
    private SearchArticleContract.View searchArticleView;
    private SearchArticleInteractor searchArticleInteractor;

    public SearchArticlePresenter(SearchArticleContract.View searchArticleView, SearchArticleInteractor searchArticleInteractor) {
        this.searchArticleView = searchArticleView;
        searchArticleView.setPresenter(this);
        this.searchArticleInteractor = searchArticleInteractor;
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
            Log.e(SearchResultPresenter.class.getName(), "onSuccess: " + throwable.getMessage());
            searchArticleView.showEmptyItem();
            searchArticleView.hideLoading();
        }
    };

    public void getArticleSearched(String searchText, int page) {
        searchArticleView.showLoading();
        if (searchText == null || searchText.replace(" ", "").isEmpty()) {
            searchArticleView.showRecentSearch();
            searchArticleView.hideLoading();
            return;
        }
        try {
            String urlEncodedText = URLEncoder.encode(searchText.trim(), "UTF-8");
            searchArticleInteractor.readSearchArticleList(page, 1, urlEncodedText, searchArticleApiCallback);
        } catch (UnsupportedEncodingException e) {
            searchArticleView.showMessage("검색결과 없습니다.");
        }

    }

    public void saveText(String text) {
        if (text == null || text.replace(" ", "").isEmpty()) return;
        if (RecentSearchSharedPreference.getInstance().getRecentSearch().isEmpty()) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(text.trim());
            RecentSearchSharedPreference.getInstance().saveRecentSearch(arrayList);
        } else {
            ArrayList<String> arrayList = RecentSearchSharedPreference.getInstance().getRecentSearch();
            if (arrayList.size() >= MAX_SAVE_SIZE) arrayList.remove(arrayList.size() - 1);
            arrayList.remove(text);// 공통된 문자가 있으면 지워준다.
            arrayList.add(0, text.trim());
            RecentSearchSharedPreference.getInstance().saveRecentSearch(arrayList);
        }
    }

}
