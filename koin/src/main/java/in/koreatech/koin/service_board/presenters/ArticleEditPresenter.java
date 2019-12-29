package in.koreatech.koin.service_board.presenters;

import in.koreatech.koin.core.bases.BasePresenter;
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

    public ArticleEditPresenter(ArticleEditContract.View articleEditView, CommunityInteractor communityInteractor) {
        this.articleEditView = articleEditView;
        this.communityInteractor = communityInteractor;
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

}

