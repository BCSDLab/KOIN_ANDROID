package in.koreatech.koin.ui.board.presenter;
import android.graphics.Bitmap;

import java.io.File;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Image;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;

public class ArticleEditPresenter {
    private final ArticleEditContract.View articleEditView;

    private final CommunityInteractor communityInteractor;
    private final MarketUsedInteractor marketUsedInteractor;

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
            articleEditView.showMessage(throwable.getMessage());
            articleEditView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Image item = (Image) object;

            if(item.getUrls() != null) {
                articleEditView.showUploadImage(item.getUrls().get(0), item.getUid());
            } else {
                articleEditView.showFailUploadImage(item.getUid());
            }

        }

        @Override
        public void onFailure(Throwable throwable) {
            articleEditView.showFailUploadImage(throwable.getMessage());
        }
    };

    public void createArticle(Article article) {
        articleEditView.showLoading();
        communityInteractor.createArticle(article, articleApiCallback);
    }

    public void updateArticle(Article article) {
        articleEditView.showLoading();
        communityInteractor.updateArticle(article, articleApiCallback);
    }

    public void createAnonymousArticle(String title, String content, String nickname, String password) {
        articleEditView.showLoading();
        communityInteractor.createAnonymousArticle(title, content, nickname, password, articleApiCallback);
    }

    public void updateAnonymousArticle(int articleId, String title, String content, String password) {
        articleEditView.showLoading();
        communityInteractor.updateAnonymousArticle(articleId, title, content, password, articleApiCallback);
    }

    public void uploadImage(File file, String uid) {
        if(file == null) {
            articleEditView.showFailUploadImage(uid);
            return;
        }
        marketUsedInteractor.uploadImage(file, uid, uploadImageApiCallback);
    }
}