package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;

public interface ArticleEditContract {
    interface View extends BaseView<ArticleEditPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void onClickEditButton();//create, edited button

        void onArticleDataReceived(Article article);

        void goToArticleActivity(Article article);
    }
}
