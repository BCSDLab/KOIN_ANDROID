package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.ui.search.presenter.SearchRecentContract;
import in.koreatech.koin.ui.search.presenter.SearchRecentPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class SearchRecentPresenterTest {

    @Mock
    private SearchRecentContract.View searchRecentView;
    @Mock
    private SearchArticleInteractor searchArticleInteractor;
    private SearchRecentPresenter searchRecentPresenter;
    private ApiCallback apiCallback;
    private ArrayList<String> recentSearch;

    @Before
    public void setSearchRecentPresenter() {
        MockitoAnnotations.initMocks(this);
        searchRecentPresenter = new SearchRecentPresenter(searchRecentView, searchArticleInteractor);
        recentSearch = new ArrayList<>();
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        searchRecentPresenter = new SearchRecentPresenter(searchRecentView, searchArticleInteractor);
        verify(searchRecentView).setPresenter(searchRecentPresenter);
    }

    @Test
    public void loadRecentDataFromLocalLoadIntoView(){
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(recentSearch);
            return null;
        }).when(searchArticleInteractor).loadSavedSearchText(any(ApiCallback.class));
        searchRecentPresenter.getRecentData();
        verify(searchRecentView).showLoading();
        verify(searchRecentView).showSearchedArticle(recentSearch);
        verify(searchRecentView).hideLoading();
    }

    @Test
    public void errorLoadRecentDataFromLocal_HideLoading(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(searchArticleInteractor).loadSavedSearchText(any(ApiCallback.class));
        searchRecentPresenter.getRecentData();
        verify(searchRecentView).showLoading();
        verify(searchRecentView).hideLoading();
    }


}
