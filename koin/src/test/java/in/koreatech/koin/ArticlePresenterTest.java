package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.ui.board.presenter.ArticleContract;
import in.koreatech.koin.ui.board.presenter.ArticlePresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class ArticlePresenterTest {

    @Mock
    private ArticleContract.View articleView;
    @Mock
    private CommunityInteractor communityInteractor;
    private ApiCallback apiCallback;
    private ArticlePresenter articlePresenter;


    @Before
    public void setupArticlePresenter() {
        MockitoAnnotations.initMocks(this);
        articlePresenter = new ArticlePresenter(articleView, communityInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        articlePresenter = new ArticlePresenter(articleView, communityInteractor);
        verify(articleView).setPresenter(articlePresenter);
    }

    @Test
    public void errorLoadAllArticleFromServer_ShowsErrorToastMessage() {
        Exception httpException = new Exception();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).readArticle(eq(0), any(ApiCallback.class));
        articlePresenter.getArticle(0);
        verify(articleView).showLoading();
        verify(articleView).showMessage(httpException.getMessage());
        verify(articleView).hideLoading();
    }

    @Test
    public void loadAllArticleFromServerAndLoadIntoView() {
        Article article = new Article();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readArticle(eq(0), any(ApiCallback.class));
        articlePresenter.getArticle(0);
        verify(articleView).showLoading();
        verify(articleView).onArticleDataReceived(article);
        verify(articleView).hideLoading();
    }

    @Test
    public void errorLoadAnonymousArticle_ShowsErrorToastMessage() {
        Exception httpException = new Exception();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).readAnonymousArticle(eq(0), any(ApiCallback.class));
        articlePresenter.getAnonymousArticle(0);
        verify(articleView).showLoading();
        verify(articleView).showMessage(httpException.getMessage());
        verify(articleView).hideLoading();
    }

    @Test
    public void loadAllAnonymousArticleFromServerAndLoadIntoView() {
        Article article = new Article();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(eq(0), any(ApiCallback.class));
        articlePresenter.getAnonymousArticle(0);
        verify(articleView).showLoading();
        verify(articleView).onArticleDataReceived(article);
        verify(articleView).hideLoading();
    }


    @Test
    public void errorDeleteArticleFromServer_ShowsErrorToastMessage() {
        Exception httpException = new Exception();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).deleteArticle(eq(0), any(ApiCallback.class));
        articlePresenter.deleteArticle(0);
        verify(articleView).showLoading();
        verify(articleView).showMessage(httpException.getMessage());
        verify(articleView).hideLoading();
    }


    @Test
    public void deleteArticleFromServerAndLoadIntoView() {
        Article article = new Article();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(eq(0), any(ApiCallback.class));
        articlePresenter.getAnonymousArticle(0);
        verify(articleView).showLoading();
        verify(articleView).onArticleDataReceived(article);
        verify(articleView).hideLoading();
    }

    @Test
    public void errorCheckArticleCheckGranted_ShowsHideEditAndDeleteMenu() {
        Exception httpException = new Exception();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).updateGrantCheck(eq(0), any(ApiCallback.class));
        articlePresenter.checkGranted(0);
        verify(articleView).showLoading();
        verify(articleView).hideEditAndDeleteMenu();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkArticleGranted_PositiveGranted_ShowsEditAndDeleteMenu() {
        Article article = new Article();
        article.setGrantEdit(true);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateGrantCheck(eq(0), any(ApiCallback.class));
        articlePresenter.checkGranted(0);
        verify(articleView).showLoading();
        verify(articleView).showEditAndDeleteMenu();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkArticleGranted_NegativeGranted_HideEditAndDeleteMenu() {
        Article article = new Article();
        article.setGrantEdit(false);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[1];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateGrantCheck(eq(0), any(ApiCallback.class));
        articlePresenter.checkGranted(0);
        verify(articleView).showLoading();
        verify(articleView).hideEditAndDeleteMenu();
        verify(articleView).hideLoading();
    }

    @Test
    public void errorDeleteAnonymousArticleFromServer_WrongPassword_ShowsErrorDeleteContent() {
        Exception httpException = new Exception();
        String wrongPassword = "12345";
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).deleteAnonymousArticle(eq(0), eq(wrongPassword), any(ApiCallback.class));
        articlePresenter.deleteAnonymousArticle(0, wrongPassword);
        verify(articleView).showLoading();
        verify(articleView).showErrorDeleteContent();
        verify(articleView).hideLoading();
    }


    @Test
    public void DeleteAnonymousArticleFromServer_MatchedPassword_ShowsSuccessDeleteContent() {
        String matchedPassword = "12345";
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onSuccess(true);
            return null;
        }).when(communityInteractor).deleteAnonymousArticle(eq(0), eq(matchedPassword), any(ApiCallback.class));
        articlePresenter.deleteAnonymousArticle(0, matchedPassword);
        verify(articleView).showLoading();
        verify(articleView).showSuccessDeleteContent();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkAnonymousAdjustArticleGranted_MatchedPassword_ShowsSuccessAdjustGrantedContent() {
        Article article = new Article();
        String wrongPassword = "12345";
        article.setGrantEdit(true);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateAnonymousGrantCheck(eq(0), eq(wrongPassword), any(ApiCallback.class));
        articlePresenter.checkAnonymousAdjustGranted(0, wrongPassword);
        verify(articleView).showLoading();
        verify(articleView).showSuccessAdjustGrantedContent();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkAnonymousAdjustGranted_WrongPassword_ShowsErrorAdjustGrantedContent() {
        Article article = new Article();
        String wrongPassword = "12345";
        article.setGrantEdit(false);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateAnonymousGrantCheck(eq(0), eq(wrongPassword), any(ApiCallback.class));
        articlePresenter.checkAnonymousAdjustGranted(0, wrongPassword);
        verify(articleView).showLoading();
        verify(articleView).showErrorAdjustGrantedContent();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkAnonymousDeleteArticleGranted_MatchedPassword_ShowsSuccessAdjustGrantedContent() {
        Article article = new Article();
        String wrongPassword = "12345";
        article.setGrantEdit(true);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateAnonymousGrantCheck(eq(0), eq(wrongPassword), any(ApiCallback.class));
        articlePresenter.checkAnonymousAdjustGranted(0, wrongPassword);
        verify(articleView).showLoading();
        verify(articleView).showSuccessAdjustGrantedContent();
        verify(articleView).hideLoading();
    }

    @Test
    public void checkAnonymousDeleteArticleGranted_WrongPassword_ShowsErrorAdjustGrantedContent() {
        Article article = new Article();
        String wrongPassword = "12345";
        article.setGrantEdit(false);
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArguments()[2];
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateAnonymousGrantCheck(eq(0), eq(wrongPassword), any(ApiCallback.class));
        articlePresenter.checkAnonymousAdjustGranted(0, wrongPassword);
        verify(articleView).showLoading();
        verify(articleView).showErrorAdjustGrantedContent();
        verify(articleView).hideLoading();
    }


}
