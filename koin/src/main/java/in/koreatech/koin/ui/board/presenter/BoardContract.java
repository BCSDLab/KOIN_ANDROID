package in.koreatech.koin.ui.board.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.response.ArticlePageResponse;

public interface BoardContract {
    interface View extends BaseView<BoardPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void updateUserInterface(ArrayList<Article> aricleArrayList);

        void onArticleGranDataReceived(Article article);

        void onBoardListDataReceived(ArticlePageResponse aricleArrayList);

        void goToArticleFragment(int articleUid, boolean isGrantEdit);

    }

}
