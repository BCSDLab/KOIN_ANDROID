package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;

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
