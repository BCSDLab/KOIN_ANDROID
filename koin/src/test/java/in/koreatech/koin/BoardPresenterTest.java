package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.data.network.response.ArticlePageResponse;
import in.koreatech.koin.ui.board.presenter.BoardContract;
import in.koreatech.koin.ui.board.presenter.BoardPresenter;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class BoardPresenterTest {

    @Mock
    private BoardContract.View boardView;
    @Mock
    private CommunityInteractor communityInteractor;
    private BoardPresenter boardPresenter;
    private ApiCallback apiCallback;
    private ArticlePageResponse articlePageResponse;
    private Article article;

    @Before
    public void setupBoardPresenter() {
        MockitoAnnotations.initMocks(this);
        boardPresenter = new BoardPresenter(boardView, communityInteractor);
        articlePageResponse = new ArticlePageResponse(new ArrayList<>(), 3);
        article = new Article();
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        boardPresenter = new BoardPresenter(boardView, communityInteractor);
        verify(boardView).setPresenter(boardPresenter);
    }

    @Test
    public void loadArticlePageFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(articlePageResponse);
            return null;
        }).when(communityInteractor).readArticleList(anyInt(), anyInt(), any(ApiCallback.class));
        boardPresenter.getArticlePage(ID_FREE,0);
        verify(boardView).onBoardListDataReceived(articlePageResponse);
        boardView.hideLoading();
    }

    @Test
    public void errorLoadArticlePageFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).readArticleList(anyInt(), anyInt(), any(ApiCallback.class));
        boardPresenter.getArticlePage(ID_FREE,0);
        verify(boardView).showMessage(exception.getMessage());
        boardView.hideLoading();
    }

    @Test
    public void loadAnonymousArticlePageFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(articlePageResponse);
            return null;
        }).when(communityInteractor).readAnonymousArticleList(anyInt(), any(ApiCallback.class));
        boardPresenter.getArticlePage(ID_ANONYMOUS,0);
        verify(boardView).onBoardListDataReceived(articlePageResponse);
        boardView.hideLoading();
    }

    @Test
    public void errorLoadAnonymousArticlePageFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).readAnonymousArticleList( anyInt(), any(ApiCallback.class));
        boardPresenter.getArticlePage(ID_ANONYMOUS,0);
        verify(boardView).showMessage(exception.getMessage());
        boardView.hideLoading();
    }

    @Test
    public void loadArticleGrantedFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateGrantCheck( anyInt(), any(ApiCallback.class));
        boardPresenter.getArticleGrant(0);
        verify(boardView).onArticleGranDataReceived(article);
        boardView.hideLoading();
    }

    @Test
    public void errorLoadArticleFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).updateGrantCheck( anyInt(), any(ApiCallback.class));
        boardPresenter.getArticleGrant(0);
        verify(boardView).showMessage(exception.getMessage());
        boardView.hideLoading();
    }

}
