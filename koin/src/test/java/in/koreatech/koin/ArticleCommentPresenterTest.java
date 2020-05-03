package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.ui.board.presenter.ArticleCommentContract;
import in.koreatech.koin.ui.board.presenter.ArticleCommentPresenter;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleCommentPresenterTest {

    @Mock
    private ArticleCommentContract.View articleCommentView;
    @Mock
    private CommunityInteractor communityInteractor;
    private ApiCallback apiCallback;
    private ArticleCommentPresenter articleCommentPresenter;
    private Comment comment;
    private Article article;


    @Before
    public void setupArticleCommentPresenter() {
        MockitoAnnotations.initMocks(this);
        articleCommentPresenter = new ArticleCommentPresenter(articleCommentView, communityInteractor);
        comment = new Comment();
        comment.setCommentUid(0);
        comment.setContent("content");
        comment.setAuthorNickname("nickname");
        comment.setPassword("1234");
        article = new Article();
        article.setBoardUid(ID_ANONYMOUS);
        article.setArticleUid(0);
        article.setContent("content");
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        articleCommentPresenter = new ArticleCommentPresenter(articleCommentView, communityInteractor);
        verify(articleCommentView).setPresenter(articleCommentPresenter);
    }

    @Test
    public void loadArticleFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readArticle(anyInt(), any(ApiCallback.class));
        articleCommentPresenter.getArticle(0);
        verify(articleCommentView).showLoading();
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void loadAnonymousArticleFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(anyInt(), any(ApiCallback.class));
        articleCommentPresenter.getAnonymousArticle(0);
        verify(articleCommentView).showLoading();
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void errorCreateComment_EmptyContent_ShowsToastMessage() {
        String content = "";
        articleCommentPresenter.createComment(0, content);
        verify(articleCommentView).showMessage(R.string.comment_input_content);

    }

    @Test
    public void errorCreateComment_NullContent_ShowsToastMessage() {
        String content = null;
        articleCommentPresenter.createComment(0, content);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }

    @Test
    public void errorCreateComment_ShowsToastMessage() {
        String content = "comment";
        Exception httpException = new Exception();
        comment.setCommentUid(0);
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(httpException);
            return null;
        }).when(communityInteractor).createComment(anyInt(), any(), any(ApiCallback.class));


        articleCommentPresenter.createComment(0, content);
        verify(articleCommentView).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_create_fail);
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void createComment_ShowsCreateComment() {
        String content = "comment";
        comment.setCommentUid(0);
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).createComment(anyInt(), any(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.createComment(0, content);
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_created);
        verify(articleCommentView).showSuccessCreateComment();
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorUpdateComment_EmptyContent_ShowsToastMessage() {
        comment.setContent("");
        articleCommentPresenter.updateComment(0, comment);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }

    @Test
    public void errorUpdateComment_NullContent_ShowsToastMessage() {
        comment.setContent(null);
        articleCommentPresenter.updateComment(0, comment);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }

    @Test
    public void errorUpdateComment_ShowsToastMessage() {
        Exception exception = new Exception();
        comment.setContent("content");
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).updateComment(any(), any(ApiCallback.class));
        articleCommentPresenter.updateComment(0, comment);
        verify(articleCommentView).showMessage(exception.getMessage());
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void updateComment_ShowsSuccessUpdateComment() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).updateComment(any(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.updateComment(0, comment);
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_edited);
        verify(articleCommentView).showSuccessEditComment();
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorDeleteComment_showToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).deleteComment(anyInt(), anyInt(), any(ApiCallback.class));
        articleCommentPresenter.deleteComment(0, 0);
        verify(articleCommentView).showMessage(R.string.comment_delete_fail);
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void deleteComment_showSuccessDeleteComment() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).deleteComment(anyInt(), anyInt(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.deleteComment(0, 0);
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_deleted);
        verify(articleCommentView).showSuccessDeleteComment();
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorCreateAnonymousComment_EmptyContent_ShowsToastMessage() {
        String content = "";
        String nickname = "nickname";
        String password = "1234";
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }

    @Test
    public void errorCreateAnonymousComment_NullContent_ShowsToastMessage() {
        String content = null;
        String nickname = "nickname";
        String password = "1234";
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }


    @Test
    public void errorCreateAnonymousComment_EmptyPassword_ShowsToastMessage() {
        String content = "content";
        String nickname = "nickname";
        String password = "";
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_password);
    }

    @Test
    public void errorCreateAnonymousComment_NullPassword_ShowsToastMessage() {
        String content = "content";
        String nickname = "nickname";
        String password = null;
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_password);
    }


    @Test
    public void errorCreateAnonymousComment_EmptyNickName_ShowsToastMessage() {
        String content = "content";
        String nickname = "";
        String password = "1234";
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_nickname);
    }

    @Test
    public void errorCreateAnonymousComment_NullNickName_ShowsToastMessage() {
        String content = "content";
        String nickname = null;
        String password = "1234";
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showMessage(R.string.comment_input_nickname);
    }


    @Test
    public void errorCreateAnonymousCommentFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        String content = "content";
        String nickname = "nickname";
        String password = "1234";
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).createAnonymousComment(anyInt(), anyString(), anyString(), anyString(), any(ApiCallback.class));
        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView).showLoading();
        verify(articleCommentView).showMessage(exception.getMessage());
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void createAnonymousCommentFromServer_ShowsCreateAnonymousComment(){
        String content = "content";
        String nickname = "nickname";
        String password = "1234";
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(4);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).createAnonymousComment(anyInt(), anyString(), anyString(), anyString(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.createAnonymousComment(0, content, nickname, password);
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_created);
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView,times(2)).hideLoading();
    }

    @Test
    public void errorUpdateAnonymousComment_EmptyContent_ShowsToastMessage() {
        comment.setContent("");
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }

    @Test
    public void errorUpdateAnonymousComment_NullContent_ShowsToastMessage() {
        comment.setContent(null);
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_content);
    }


    @Test
    public void errorUpdateAnonymousComment_EmptyPassword_ShowsToastMessage() {
        comment.setPassword("");
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_password);
    }

    @Test
    public void errorUpdateAnonymousComment_NullPassword_ShowsToastMessage() {
        comment.setPassword(null);
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_password);
    }


    @Test
    public void errorUpdateAnonymousComment_EmptyNickName_ShowsToastMessage() {
        comment.setAuthorNickname("");
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_nickname);
    }

    @Test
    public void errorUpdateAnonymousComment_NullNickName_ShowsToastMessage() {
        comment.setAuthorNickname(null);
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showMessage(R.string.comment_input_nickname);
    }


    @Test
    public void errorUpdateAnonymousCommentFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).updateAnonymousComment(any(), any(ApiCallback.class));
        articleCommentPresenter.updateAnonymousComment(0,comment);
        verify(articleCommentView).showLoading();
        verify(articleCommentView).showMessage(exception.getMessage());
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void createUpdateCommentFromServer_ShowsCreateAnonymousComment(){
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).updateAnonymousComment(any(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.updateAnonymousComment(0, comment);
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_edited);
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView,times(2)).hideLoading();
    }

    @Test
    public void errorDeleteAnonymousCommentFromServer_ShowsToastMessage(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(communityInteractor).deleteAnonymousComment(anyInt(), anyInt(), any(), any(ApiCallback.class));

        articleCommentPresenter.deleteAnonymousComment(comment.getArticleUid(),comment.getCommentUid(), comment.getPassword());
        verify(articleCommentView).showLoading();
        verify(articleCommentView).showMessage(exception.getMessage());
        verify(articleCommentView).hideLoading();
    }

    @Test
    public void deleteAnonymousCommentFromServer_ShowsSuccessAnonymousDeleteComment(){
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(comment);
            return null;
        }).when(communityInteractor).deleteAnonymousComment(anyInt(), anyInt(), any(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(article);
            return null;
        }).when(communityInteractor).readAnonymousArticle(anyInt(), any(ApiCallback.class));

        articleCommentPresenter.deleteAnonymousComment(comment.getArticleUid(),comment.getCommentUid(), comment.getPassword());
        verify(articleCommentView, times(2)).showLoading();
        verify(articleCommentView).showMessage(R.string.comment_deleted);
        verify(articleCommentView).onArticleDataReceived(article);
        verify(articleCommentView,times(2)).hideLoading();
    }


}
