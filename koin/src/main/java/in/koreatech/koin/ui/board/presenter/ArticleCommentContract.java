package in.koreatech.koin.ui.board.presenter;

import androidx.annotation.StringRes;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;


public interface ArticleCommentContract {
    interface View extends BaseView<ArticleCommentPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void showMessage(@StringRes int message);

        void updateUserInterface();

        void showSuccessCreateComment();

        void showSuccessDeleteComment();

        void showSuccessUpdateAnonymousComment();

        void showSuccessCreateAnonymousComment();

        void showSuccessAnonymousDeleteComment();

        void showSuccessEditComment();

        void onArticleDataReceived(Article article);
    }
}
