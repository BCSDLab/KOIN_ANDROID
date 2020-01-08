package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;

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
