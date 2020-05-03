package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import retrofit2.HttpException;

public class ArticleCommentPresenter {
    private final ArticleCommentContract.View articleCommentView;

    private final CommunityInteractor communityInteractor;

    public ArticleCommentPresenter(ArticleCommentContract.View articleCommentView, CommunityInteractor communityInteractor) {
        this.articleCommentView = articleCommentView;
        this.communityInteractor = communityInteractor;
        articleCommentView.setPresenter(this);
    }


    private final ApiCallback commentApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            getArticle(articleUid);
        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    };

    private final ApiCallback commentCreateApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_created);
            articleCommentView.showSuccessCreateComment();
            getArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleCommentView.showMessage(R.string.comment_create_fail);
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback commentEditApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_edited);
            articleCommentView.showSuccessEditComment();
            getArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable instanceof HttpException)
                articleCommentView.showMessage(R.string.comment_edit_fail);
            else
                articleCommentView.showMessage(throwable.getMessage());
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback commentDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_deleted);
            articleCommentView.showSuccessDeleteComment();
            getArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleCommentView.showMessage(R.string.comment_delete_fail);
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback commentAnoymousApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_created);
            articleCommentView.showSuccessCreateAnonymousComment();
            getAnonymousArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable instanceof HttpException)
                articleCommentView.showMessage(R.string.comment_edit_fail);
            else
                articleCommentView.showMessage(throwable.getMessage());
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback commentAnoymousUpdateApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_edited);
            articleCommentView.showSuccessUpdateAnonymousComment();
            getAnonymousArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable instanceof HttpException)
                articleCommentView.showMessage(R.string.comment_edit_fail);
            else
                articleCommentView.showMessage(throwable.getMessage());
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback commentAnoymousDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            int articleUid = ((Comment) object).getArticleUid();
            articleCommentView.showMessage(R.string.comment_deleted);
            articleCommentView.showSuccessAnonymousDeleteComment();
            getAnonymousArticle(articleUid);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            if (throwable instanceof HttpException)
                articleCommentView.showMessage(R.string.comment_delete_fail);
            else
                articleCommentView.showMessage(throwable.getMessage());
            articleCommentView.hideLoading();
        }
    };

    private final ApiCallback articleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Article article = (Article) (object);
            articleCommentView.onArticleDataReceived(article);
            articleCommentView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleCommentView.showMessage(throwable.getMessage());
            articleCommentView.hideLoading();
        }
    };

    public void getArticle(int articleUid) {
        articleCommentView.showLoading();
        communityInteractor.readArticle(articleUid, articleApiCallback);
    }

    public void getAnonymousArticle(int articleUid) {
        articleCommentView.showLoading();
        communityInteractor.readAnonymousArticle(articleUid, articleApiCallback);
    }

    public void createComment(int articleUid, String content) {
        if (FormValidatorUtil.validateStringIsEmpty(content)) {
            articleCommentView.showMessage(R.string.comment_input_content);
            return;
        }
        articleCommentView.showLoading();
        communityInteractor.createComment(articleUid, content, commentCreateApiCallback);
    }

    public void updateComment(int articleUid, Comment comment) {
        if (FormValidatorUtil.validateStringIsEmpty(comment.getContent())) {
            articleCommentView.showMessage(R.string.comment_input_content);
            return;
        }
        articleCommentView.showLoading();
        comment.setArticleUid(articleUid);
        communityInteractor.updateComment(comment, commentEditApiCallback);
    }

    public void deleteComment(int articleUid, int commentUid) {
        articleCommentView.showLoading();
        communityInteractor.deleteComment(articleUid, commentUid, commentDeleteApiCallback);
    }


    public void createAnonymousComment(int articleUid, String content, String nickname, String password) {
        if (FormValidatorUtil.validateStringIsEmpty(content)) {
            articleCommentView.showMessage(R.string.comment_input_content);
        } else if (FormValidatorUtil.validateStringIsEmpty(password)) {
            articleCommentView.showMessage(R.string.comment_input_password);
        } else if (FormValidatorUtil.validateStringIsEmpty(nickname)) {
            articleCommentView.showMessage(R.string.comment_input_nickname);
        } else {
            articleCommentView.showLoading();
            communityInteractor.createAnonymousComment(articleUid, content, nickname, password, commentAnoymousApiCallback);
        }
    }

    public void updateAnonymousComment(int articleUid, Comment comment) {
        if (FormValidatorUtil.validateStringIsEmpty(comment.getContent())) {
            articleCommentView.showMessage(R.string.comment_input_content);
        } else if (FormValidatorUtil.validateStringIsEmpty(comment.getPassword())) {
            articleCommentView.showMessage(R.string.comment_input_password);
        } else if (FormValidatorUtil.validateStringIsEmpty(comment.getAuthorNickname())) {
            articleCommentView.showMessage(R.string.comment_input_nickname);
        } else {
            articleCommentView.showLoading();
            comment.setArticleUid(articleUid);
            communityInteractor.updateAnonymousComment(comment, commentAnoymousUpdateApiCallback);
        }
    }

    public void deleteAnonymousComment(int articleUid, int commentUid, String password) {
        articleCommentView.showLoading();
        communityInteractor.deleteAnonymousComment(articleUid, commentUid, password, commentAnoymousDeleteApiCallback);
    }
}
