package in.koreatech.koin.service_board.contracts;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.service_board.presenters.ArticlePresenter;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface ArticleContract {
    interface View extends BaseView<ArticlePresenter> {
        void checkRequiredInfo();

        void showLoading();
        void hideLoading();
        void showMessage(String message);

        void updateUserInterface();

        void onArticleDataReceived(Article article);
        void onArticleDeleteCompleteReceived(boolean isSuccess);

        void onClickEditButton();
        void onClickRemoveButton();
//        void onClickCommentModifyButton(Comment comment);
//        void onClickCommentRemoveButton(final Comment comment);

//        void makeEditCommentDialog(final Comment comment);

        void showEditAndDeleteMenu();
        void hideEditAndDeleteMenu();

//        void showErrorDeleteComment();
//        void showSuccessDeleteComment();
//
//        void showErrorGrantedDeleteComment();
//        void showSuccessGrantedDeleteComment();

        void showErrorGrantedDeleteContent();
        void showSuccessGrantedDeleteContent();

//        void showErrorGrantedAdjustComment();
//        void showSuccessGrantedAdjustComment();
//
//        void showErrorAdjustComment();
//        void showSuccessAdjustComment();

        void showErrorDeleteContent();
        void showSuccessDeleteContent();

        void showErrorAdjustGrantedContent();
        void showSuccessAdjustGrantedContent();
    }
}
