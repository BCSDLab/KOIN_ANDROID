package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Image;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.ui.board.presenter.ArticleEditContract;
import in.koreatech.koin.ui.board.presenter.ArticleEditPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleEditPresenterTest {

    @Mock
    private ArticleEditContract.View articleEditView;
    @Mock
    private CommunityInteractor communityInteractor;
    @Mock
    private MarketUsedInteractor marketUsedInteractor;
    private ArticleEditPresenter articleEditPresenter;
    private ApiCallback apiCallback;
    private Article article;
    private File file;
    private Image image;
    private String URI;

    @Before
    public void setupArticleEditPresenter() {
        MockitoAnnotations.initMocks(this);
        ArrayList<String> urls = new ArrayList<>();
        articleEditPresenter = new ArticleEditPresenter(articleEditView, communityInteractor, marketUsedInteractor);
        article = new Article();
        article.setBoardUid(0);
        article.setArticleUid(0);
        article.setAuthorNickname("nickname");
        article.setTitle("title");
        article.setContent("<u>this is the content</u>");
        image = new Image();
        urls.add("https://www.image.com");
        image.setUrls(urls);
        URI = "";
        file = new File(URI);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        articleEditPresenter = new ArticleEditPresenter(articleEditView, communityInteractor, marketUsedInteractor);
        verify(articleEditView).setPresenter(articleEditPresenter);
    }

    @Test
    public void errorCreateArticle_EmptyTitle_ShowsToastMessage() {
        article.setTitle("");
        articleEditPresenter.createArticle(article);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorCreateArticle_NullTitle_ShowsToastMessage() {
        article.setTitle(null);
        articleEditPresenter.createArticle(article);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorCreateArticle_EmptyContent_ShowsToastMessage() {
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>",
                "<p data-tag=\"input\" style=\"color:#000000;\"></p>",
                "<!-- Not Allowed Attribute Filtered ( data-tag=\"input\") --><p style=\"color:#000000;\"></p>",
                "<!-- Not Allowed Attribute Filtered ( data-tag=\"input\") --><p style=\"color:#000000;\"></p><u></u><br>",
                "<!-- -->"
        };

        for (String content : emptyHtmlContent) {
            article.setContent(content);
            articleEditPresenter.createArticle(article);
        }
        verify(articleEditView, times(emptyHtmlContent.length)).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorCreateArticle_NullContent_ShowsToastMessage() {
        article.setContent(null);
        articleEditPresenter.createArticle(article);
        verify(articleEditView).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorCreateArticleFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).createArticle(any(), any(ApiCallback.class));
        articleEditPresenter.createArticle(article);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).showMessage(R.string.board_update_fail);
        verify(articleEditView).blockButtonClick(false);
        verify(articleEditView).hideLoading();
    }

    @Test
    public void createArticleFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).createArticle(any(), any(ApiCallback.class));
        articleEditPresenter.createArticle(article);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).onArticleDataReceived(article);
        verify(articleEditView).hideLoading();
    }

    @Test
    public void errorUpdateArticle_EmptyTitle_ShowsToastMessage() {
        article.setTitle("");
        articleEditPresenter.updateArticle(article);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorUpdateArticle_NullTitle_ShowsToastMessage() {
        article.setTitle(null);
        articleEditPresenter.updateArticle(article);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorUpdateArticle_EmptyContent_ShowsToastMessage() {
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>"
        };

        for (String content : emptyHtmlContent) {
            article.setContent(content);
            articleEditPresenter.updateArticle(article);
        }
        verify(articleEditView, times(emptyHtmlContent.length)).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorUpdateArticle_NullContent_ShowsToastMessage() {
        article.setContent(null);
        articleEditPresenter.updateArticle(article);
        verify(articleEditView).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorUpdateArticleFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).updateArticle(any(), any(ApiCallback.class));
        articleEditPresenter.updateArticle(article);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).showMessage(R.string.board_update_fail);
        verify(articleEditView).blockButtonClick(false);
        verify(articleEditView).hideLoading();
    }

    @Test
    public void updateArticleFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateArticle(any(), any(ApiCallback.class));
        articleEditPresenter.updateArticle(article);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).onArticleDataReceived(article);
        verify(articleEditView).hideLoading();
    }

    @Test
    public void errorCreateAnonymousArticle_EmptyTitle_ShowsToastMessage() {
        String password = "1234";
        article.setTitle("");
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorCreateAnonymousArticle_NullTitle_ShowsToastMessage() {
        String password = "1234";
        article.setTitle(null);
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorCreateAnonymousArticle_EmptyContent_ShowsToastMessage() {
        String password = "1234";
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>",
                "<!-- Not Allowed Attribute Filtered ( data-tag=\"input\") --><p style=\"color:#000000;\"> </p><u></u> <br>",
        };

        for (String content : emptyHtmlContent) {
            article.setContent(content);
            articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        }
        verify(articleEditView, times(emptyHtmlContent.length)).showMessage(R.string.board_content_check);
    }


    @Test
    public void errorCreateAnonymousArticle_NullContent_ShowsToastMessage() {
        String password = "1234";
        article.setContent(null);
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorCreateAnonymousArticle_NullNickName_ShowsToastMessage() {
        String password = "1234";
        article.setAuthorNickname("");
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_nickname_check);
    }


    @Test
    public void errorCreateAnonymousArticle_EmptyNickName_ShowsToastMessage() {
        String password = "1234";
        article.setAuthorNickname(null);
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_nickname_check);
    }

    @Test
    public void errorCreateAnonymousArticle_EmptyPassword_ShowsToastMessage() {
        String password = "";
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_password_check);
    }

    @Test
    public void errorCreateAnonymousArticle_NullPassword_ShowsToastMessage() {
        String password = null;
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).showMessage(R.string.board_password_check);
    }

    @Test
    public void errorCreateAnonymousArticleFromServer_ShowsToastMessage() {
        String password = "1234";
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).createAnonymousArticle(any(), any(), any(), any(), any(ApiCallback.class));
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).showMessage(R.string.board_update_fail);
        verify(articleEditView).blockButtonClick(false);
        verify(articleEditView).hideLoading();
    }


    @Test
    public void AnonymousArticleFromServerLoadIntoView() {
        String password = "1234";
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).createAnonymousArticle(any(), any(), any(), any(), any(ApiCallback.class));
        articleEditPresenter.createAnonymousArticle(article.getTitle(), article.getContent(), article.getAuthorNickname(), password);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).onArticleDataReceived(article);
        verify(articleEditView).hideLoading();
    }

//TODO

    @Test
    public void errorUpdateAnonymousArticle_EmptyTitle_ShowsToastMessage() {
        String password = "1234";
        article.setTitle("");
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorUpdateAnonymousArticle_NullTitle_ShowsToastMessage() {
        String password = "1234";
        article.setTitle(null);
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).showMessage(R.string.board_title_check);
    }

    @Test
    public void errorUpdateAnonymousArticle_EmptyContent_ShowsToastMessage() {
        String password = "1234";
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>",
                "<p data-tag=\"input\" style=\"color:#000000;\"></p>",
                "<!-- Not Allowed Attribute Filtered ( data-tag=\"input\") --><p style=\"color:#000000;\"></p>",
                "<!-- Not Allowed Attribute Filtered ( data-tag=\"input\") --><p style=\"color:#000000;\"></p><u></u><br>",
                "<!-- -->"
        };

        for (String content : emptyHtmlContent) {
            article.setContent(content);
            articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        }
        verify(articleEditView, times(emptyHtmlContent.length)).showMessage(R.string.board_content_check);
    }


    @Test
    public void errorUpdateAnonymousArticle_NullContent_ShowsToastMessage() {
        String password = "1234";
        article.setContent(null);
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).showMessage(R.string.board_content_check);
    }

    @Test
    public void errorUpdateAnonymousArticle_EmptyPassword_ShowsToastMessage() {
        String password = "";
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).showMessage(R.string.board_password_check);
    }

    @Test
    public void errorUpdateAnonymousArticle_NullPassword_ShowsToastMessage() {
        String password = null;
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).showMessage(R.string.board_password_check);
    }

    @Test
    public void errorUpdateAnonymousArticleFromServer_ShowsToastMessage() {
        String password = "1234";
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).updateAnonymousArticle(anyInt(), any(), any(), any(), any(ApiCallback.class));
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).showMessage(R.string.board_update_fail);
        verify(articleEditView).blockButtonClick(false);
        verify(articleEditView).hideLoading();
    }


    @Test
    public void updateAnonymousArticleFromServerLoadIntoView() {
        String password = "1234";
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).updateAnonymousArticle(anyInt(), any(), any(), any(), any(ApiCallback.class));
        articleEditPresenter.updateAnonymousArticle(article.getArticleUid(), article.getTitle(), article.getContent(), password);
        verify(articleEditView).blockButtonClick(true);
        verify(articleEditView).showLoading();
        verify(articleEditView).onArticleDataReceived(article);
        verify(articleEditView).hideLoading();
    }

    @Test
    public void errorUploadImage_NullFile_ShowsFailUploadImage() {
        String uid = "0";
        file = null;
        articleEditPresenter.uploadImage(file, uid);
        verify(articleEditView).showFailUploadImage(uid);
    }

    @Test
    public void errorUploadImageFromServer_ShowsFailUploadImage() {
        String uid = "0";
        image.setUrls(null);
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(image);
            return null;
        }).when(marketUsedInteractor).uploadImage(any(), any(ApiCallback.class));
        articleEditPresenter.uploadImage(file, uid);
        verify(articleEditView).showFailUploadImage(uid);
    }

    @Test
    public void uploadImageToServerLoadIntoView() {
        String uid = "0";
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(image);
            return null;
        }).when(marketUsedInteractor).uploadImage(any(), any(ApiCallback.class));
        articleEditPresenter.uploadImage(file, uid);
        verify(articleEditView).showUploadImage(image.getUrls().get(0), uid);
    }

}
