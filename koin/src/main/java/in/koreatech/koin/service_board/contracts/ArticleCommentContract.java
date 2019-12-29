package in.koreatech.koin.service_board.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.service_board.presenters.ArticleCommentPresenter;
import in.koreatech.koin.service_board.presenters.ArticlePresenter;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface ArticleCommentContract {
    interface View extends BaseView<ArticleCommentPresenter> {
        void showLoading();
        void hideLoading();
        void showMessage(String message);

        void updateUserInterface();
        void onClickCommentModifyButton(Comment comment);
        void onClickCommentRemoveButton(final Comment comment);

        void showErrorDeleteComment();
        void showSuccessDeleteComment();

        void showErrorDeleteAnonymousComment();
        void showSuccessAnonymousDeleteComment();

        void showErrorUpdateAnonymousComment();
        void showSuccessUpdateAnonymousComment();

        void showSuccessCreateComment();
        void showErrorCreateComment();

        void showSuccessCreateAnonymousComment();
        void showErrorCreateAnonymousComment();

        void showErrorGrantedDeleteComment();
        void showSuccessGrantedDeleteComment();

        void showErrorGrantedDeleteContent();
        void showSuccessGrantedDeleteContent();

        void showErrorGrantedAdjustComment();
        void showSuccessGrantedAdjustComment();

        void showErrorEditComment();
        void showSuccessEditComment();

        void onArticleDataReceived(Article article);
    }
}
