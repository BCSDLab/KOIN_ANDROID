package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.ui.search.presenter.SearchArticleContract;
import in.koreatech.koin.ui.search.presenter.SearchArticlePresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchArticlePresenterTest {
    @Mock
    private SearchArticleContract.View searchArticleView;
    @Mock
    private SearchArticleInteractor searchArticleInteractor;
    @Mock
    private SearchedArticle searchedArticle;
    private SearchArticlePresenter searchArticlePresenter;
    private ApiCallback apiCallback;

    @Before
    public void setSearchArticlePresenter() {
        MockitoAnnotations.initMocks(this);
        searchArticlePresenter = new SearchArticlePresenter(searchArticleView, searchArticleInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        searchArticlePresenter = new SearchArticlePresenter(searchArticleView, searchArticleInteractor);
        verify(searchArticleView).setPresenter(searchArticlePresenter);
    }



    @Test
    public void loadArticleSearchedFromServer_NotEmptyItem_LoadIntoView() {
        String searchText = "검색텍스트";
        String encodedText = "%EA%B2%80%EC%83%89%ED%85%8D%EC%8A%A4%ED%8A%B8";
        int page = 1;
        ArrayList<SearchedArticle> articles = new ArrayList<>();
        articles.add(new SearchedArticle());
        when(searchedArticle.getSearchedArticles()).thenReturn(articles);
        doAnswer(invocation -> {
            assertEquals("text need to be encoded", invocation.getArgument(2),encodedText);
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(searchedArticle);
            return null;
        }).when(searchArticleInteractor).readSearchArticleList(anyInt(), anyInt(), anyString(), any(ApiCallback.class));
        searchArticlePresenter.getArticleSearched(searchText, page);
        verify(searchArticleView).showSearchedArticle(searchedArticle);
        verify(searchArticleView).hideLoading();
    }

    @Test
    public void loadArticleSearchedFromServer_EmptyItem_ShowsEmptyItem() {
        String searchText = "검색텍스트";
        String encodedText = "%EA%B2%80%EC%83%89%ED%85%8D%EC%8A%A4%ED%8A%B8";
        int page = 1;
        doAnswer(invocation -> {
            assertEquals("text need to be encoded", invocation.getArgument(2),encodedText);
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(searchedArticle);
            return null;
        }).when(searchArticleInteractor).readSearchArticleList(anyInt(), anyInt(), anyString(), any(ApiCallback.class));
        searchArticlePresenter.getArticleSearched(searchText, page);
        verify(searchArticleView).showEmptyItem();
        verify(searchArticleView).hideLoading();
    }

    @Test
    public void loadArticle_EmptyString_ShowsRecentSearch(){
        String searchText = "";
        searchArticlePresenter.getArticleSearched(searchText, 1);
        verify(searchArticleView).showLoading();
        verify(searchArticleView).showRecentSearch();
        verify(searchArticleView).hideLoading();
    }

    @Test
    public void loadArticle_NullString_ShowsRecentSearch(){
        String searchText = null;
        searchArticlePresenter.getArticleSearched(searchText, 1);
        verify(searchArticleView).showLoading();
        verify(searchArticleView).showRecentSearch();
        verify(searchArticleView).hideLoading();
    }

    @Test
    public void errorSaveSearchedTextToLocal_ShowsToastMessage(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(searchArticleInteractor).saveSearchText(anyString(), any(ApiCallback.class));
        searchArticlePresenter.saveSearchedText("text");
        verify(searchArticleView).showMessage(R.string.search_save_recent_fail);
    }


}
