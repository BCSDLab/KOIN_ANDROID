package in.koreatech.koin.service_board.presenters;


import android.util.Log;

import java.io.File;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.entity.Image;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.interactors.MarketUsedInteractor;
import in.koreatech.koin.core.networks.interactors.MarketUsedRestInteractor;
import in.koreatech.koin.service_board.contracts.ArticleEditContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.CommunityInteractor;

/**
 * Created by hyerim on 2018. 6. 7....
 */
public class ArticleEditPresenter implements BasePresenter {
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
            articleEditView.showMessage(throwable.getMessage());
            articleEditView.hideLoading();
        }
    };

    private final ApiCallback uploadImageApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Image item = (Image) object;
            if(item.getUrls() != null)
                articleEditView.showUploadImage(item.getUrls().get(0),uploadImageId);
            else
                articleEditView.showFailUploadImage(uploadImageId);

        }

        @Override
        public void onFailure(Throwable throwable) {
            articleEditView.showFailUploadImage(uploadImageId);
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
        uploadImageId = uid;
        if(file == null)
        {
            articleEditView.showFailUploadImage(uid);
            return;
        }
        marketUsedInteractor.uploadImage(file, uploadImageApiCallback);
    }

}

