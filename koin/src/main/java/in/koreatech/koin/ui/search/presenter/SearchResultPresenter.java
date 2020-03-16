package in.koreatech.koin.ui.search.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;

public class SearchResultPresenter{

    private SearchResultContract.View searchResultView;
    private SearchArticleInteractor searchArticleInteractor;

    public SearchResultPresenter(SearchResultContract.View searchResultView, SearchArticleInteractor searchArticleInteractor) {
        this.searchResultView = searchResultView;
        this.searchArticleInteractor = searchArticleInteractor;
        searchResultView.setPresenter(this);
    }

    private final ApiCallback searchArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            SearchedArticle searchedArticle = (SearchedArticle) object;
            if(!searchedArticle.getSearchedArticles().isEmpty()) {
                searchResultView.showSearchedArticle(searchedArticle);

                searchResultView.hideLoading();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            searchResultView.showMessage("검색결과를 가져올 수 없습니다.");
            searchResultView.hideLoading();
        }
    };

    public void getArticleSearched(String searchText, int page) {
        searchResultView.showLoading();
        searchArticleInteractor.readSearchArticleList(page, 1, searchText.trim(), searchArticleApiCallback);
    }
}
