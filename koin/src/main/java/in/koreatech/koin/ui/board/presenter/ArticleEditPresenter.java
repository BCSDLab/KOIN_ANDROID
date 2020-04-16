package in.koreatech.koin.ui.board.presenter;

import java.io.File;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Image;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;

public class ArticleEditPresenter {
    private final ArticleEditContract.View articleEditView;

    private final CommunityInteractor communityInteractor;
    private final MarketUsedInteractor marketUsedInteractor;
    private String uploadImageId;

    public ArticleEditPresenter(ArticleEditContract.View articleEditView, CommunityInteractor communityInteractor) {
        this.articleEditView = articleEditView;
        this.communityInteractor = communityInteractor;
        // TODO -> API 변경시 바꾸기
        this.marketUsedInteractor = new MarketUsedRestInteractor();
    }

    private final ApiCallback articleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            articleEditView.onArticleDataReceived((Article) object);
            articleEditView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleEditView.showMessage(R.string.board_update_fail);
            articleEditView.blockButtonClick(false);
            articleEditView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Image item = (Image) object;

            if (item.getUrls() != null) {
                articleEditView.showUploadImage(item.getUrls().get(0), uploadImageId);
            } else {
                articleEditView.showFailUploadImage(uploadImageId);
            }

        }

        @Override
        public void onFailure(Throwable throwable) {
            articleEditView.showFailUploadImage(uploadImageId);
        }
    };

    public void createArticle(Article article) {
        if (FormValidatorUtil.validateStringIsEmpty(article.getTitle())) {
            articleEditView.showMessage(R.string.board_title_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(article.getContent())) {
            articleEditView.showMessage(R.string.board_content_check);
            return;
        }
        articleEditView.blockButtonClick(true);
        articleEditView.showLoading();
        communityInteractor.createArticle(article, articleApiCallback);
    }

    public void updateArticle(Article article) {
        if (FormValidatorUtil.validateStringIsEmpty(article.getTitle())) {
            articleEditView.showMessage(R.string.board_title_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(article.getContent())) {
            articleEditView.showMessage(R.string.board_content_check);
            return;
        }

        articleEditView.blockButtonClick(true);
        articleEditView.showLoading();
        communityInteractor.updateArticle(article, articleApiCallback);
    }

    public void createAnonymousArticle(String title, String content, String nickname, String password) {
        if (FormValidatorUtil.validateStringIsEmpty(title)) {
            articleEditView.showMessage(R.string.board_title_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(content)) {
            articleEditView.showMessage(R.string.board_content_check);
            return;
        } else if (FormValidatorUtil.validateStringIsEmpty(password)) {
            articleEditView.showMessage(R.string.board_password_check);
            return;
        } else if (FormValidatorUtil.validateStringIsEmpty(nickname)) {
            articleEditView.showMessage(R.string.board_nickname_check);
            return;
        }

        articleEditView.blockButtonClick(true);
        articleEditView.showLoading();
        communityInteractor.createAnonymousArticle(title, content, nickname, password, articleApiCallback);
    }

    public void updateAnonymousArticle(int articleId, String title, String content, String password) {
        if (FormValidatorUtil.validateStringIsEmpty(title)) {
            articleEditView.showMessage(R.string.board_title_check);
            return;
        } else if (FormValidatorUtil.validateHTMLStringIsEmpty(content)) {
            articleEditView.showMessage(R.string.board_content_check);
            return;
        } else if (FormValidatorUtil.validateStringIsEmpty(password)) {
            articleEditView.showMessage(R.string.board_password_check);
            return;
        }

        articleEditView.blockButtonClick(true);
        articleEditView.showLoading();
        communityInteractor.updateAnonymousArticle(articleId, title, content, password, articleApiCallback);
    }

    public void uploadImage(File file, String uid) {
        uploadImageId = uid;
        if (file == null) {
            articleEditView.showFailUploadImage(uid);
            return;
        }
        marketUsedInteractor.uploadImage(file, uploadImageApiCallback);
    }
}