package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleInteractor;
import in.koreatech.koin.ui.search.presenter.SearchResultContract;
import in.koreatech.koin.ui.search.presenter.SearchResultPresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchResultPresenterTest {

    @Mock
    private SearchResultContract.View searchRecentView;
    @Mock
    private SearchArticleInteractor searchArticleInteractor;
    @Mock
    private SearchedArticle searchedArticle;
    private SearchResultPresenter searchResultPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setSearchResultPresenter() {
        MockitoAnnotations.initMocks(this);
        searchResultPresenter = new SearchResultPresenter(searchRecentView, searchArticleInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        searchResultPresenter = new SearchResultPresenter(searchRecentView, searchArticleInteractor);
        verify(searchRecentView).setPresenter(searchResultPresenter);
    }

    @Test
    public void errorLoadSearchArticleFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        String searchText = "search Text ";
        doAnswer(invocation -> {
            assertEquals("Text need to be trim", invocation.getArgument(2), searchText.trim());
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(searchArticleInteractor).readSearchArticleList(anyInt(), anyInt(), anyString(), any(ApiCallback.class));
        searchResultPresenter.getArticleSearched(searchText, 1);
        verify(searchRecentView).showLoading();
        verify(searchRecentView).showMessage(R.string.search_result_fail);
        verify(searchRecentView).hideLoading();
    }

    @Test
    public void loadSearchArticleFromServerLoadIntoView() {
        String searchText = "search Text ";
        SearchedArticle article = new SearchedArticle();
        ArrayList<SearchedArticle> articles = new ArrayList<>();
        articles.add(article);
        when(searchedArticle.getSearchedArticles()).thenReturn(articles);
        doAnswer(invocation -> {
            assertEquals("Text need to be trim", invocation.getArgument(2), searchText.trim());
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(searchedArticle);
            return null;
        }).when(searchArticleInteractor).readSearchArticleList(anyInt(), anyInt(), anyString(),  any(ApiCallback.class));
        searchResultPresenter.getArticleSearched(searchText, 1);
        verify(searchRecentView).showLoading();
        verify(searchRecentView).showSearchedArticle(searchedArticle);
        verify(searchRecentView).hideLoading();
    }


}


