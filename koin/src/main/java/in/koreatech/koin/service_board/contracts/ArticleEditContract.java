package in.koreatech.koin.service_board.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.service_board.presenters.ArticleEditPresenter;

/**
 * Created by hyerim on 2018. 6. 7....
 */
public interface ArticleEditContract {
    interface View extends BaseView<ArticleEditPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onClickEditButton();//create, edited button

        void onArticleDataReceived(Article article);

        void goToArticleActivity(Article article);

        void showUploadImage(String url, String uploadImageId);

        void showFailUploadImage(String uploadImageId);
    }
}
