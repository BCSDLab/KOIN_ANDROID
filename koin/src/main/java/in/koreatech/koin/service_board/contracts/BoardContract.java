package in.koreatech.koin.service_board.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.responses.ArticlePageResponse;
import in.koreatech.koin.service_board.presenters.BoardPresenter;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface BoardContract {
    interface View extends BaseView<BoardPresenter> {
        void showLoading();
        void hideLoading();
        void showMessage(String message);

        void updateUserInterface(ArrayList<Article> aricleArrayList);

        void onArticleGranDataReceived(Article article);
        void onBoardListDataReceived(ArticlePageResponse aricleArrayList);

        void goToArticleActivity(int articleUid, boolean isGrantEdit);



    }

}
