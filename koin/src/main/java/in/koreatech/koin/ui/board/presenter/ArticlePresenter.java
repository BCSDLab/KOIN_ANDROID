package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;

public class ArticlePresenter {
    private final ArticleContract.View articleView;

    private final CommunityInteractor communityInteractor;

    public ArticlePresenter(ArticleContract.View articleView, CommunityInteractor communityInteractor) {
        this.articleView = articleView;
        this.communityInteractor = communityInteractor;
    }

    private final ApiCallback articleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Article article = (Article) (object);
            articleView.onArticleDataReceived(article);
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showMessage(throwable.getMessage());
            articleView.hideLoading();
        }
    };

    private final ApiCallback deleteArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            articleView.onArticleDeleteCompleteReceived(true);
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showMessage(throwable.getMessage());
            articleView.hideLoading();
        }
    };

    private final ApiCallback grantUpdateApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit();
            if (isGrantEdit)
                articleView.showEditAndDeleteMenu();
            else
                articleView.hideEditAndDeleteMenu();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.hideEditAndDeleteMenu();
            articleView.hideLoading();
        }
    };

    private final ApiCallback grantAnonymousAdjustApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit();
            if (isGrantEdit)
                articleView.showSuccessAdjustGrantedContent();
            else
                articleView.showErrorAdjustGrantedContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorAdjustGrantedContent();
            articleView.hideLoading();
        }
    };

    private final ApiCallback grantAnonymousDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit();
            if (isGrantEdit)
                articleView.showSuccessGrantedDeleteContent();
            else
                articleView.showErrorGrantedDeleteContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorGrantedDeleteContent();
            articleView.hideLoading();
        }
    };
    private final ApiCallback deleteAnonymousArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            articleView.showSuccessDeleteContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorDeleteContent();
            articleView.hideLoading();
        }
    };

    public void getArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.readArticle(articleUid, articleApiCallback);
    }

    public void getAnonymousArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.readAnonymousArticle(articleUid, articleApiCallback);
    }

    public void deleteArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.deleteArticle(articleUid, deleteArticleApiCallback);
    }

    public void checkGranted(int articleUid) {
        articleView.showLoading();
        communityInteractor.updateGrantCheck(articleUid, grantUpdateApiCallback);
    }

    public void deleteAnoymousArticle(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.deleteAnonymousArticle(articleUid, password, deleteAnonymousArticleApiCallback);
    }

    public void checkAnonymousAdjustGranted(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.updateAnonymousGrantCheck(articleUid, password, grantAnonymousAdjustApiCallback);
    }

    public void checkAnonymousDeleteGranted(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.updateAnonymousGrantCheck(articleUid, password, grantAnonymousDeleteApiCallback);
    }


}
